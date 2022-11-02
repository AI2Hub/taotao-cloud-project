/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.web.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.querydsl.core.types.Predicate;
import com.taotao.cloud.cache.redis.model.CacheKey;
import com.taotao.cloud.cache.redis.model.CacheKeyBuilder;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.exception.IdempotencyException;
import com.taotao.cloud.common.exception.LockException;
import com.taotao.cloud.lock.support.DistributedLock;
import com.taotao.cloud.lock.support.ZLock;
import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import com.taotao.cloud.web.base.service.AbstractBaseSuperService;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * BaseService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/10 10:21
 */
public class BaseSuperServiceImpl<
	M extends BaseSuperMapper<T, I>,
	T extends SuperEntity<T, I>,
	CR extends BaseClassSuperRepository<T, I>,
	IR extends JpaRepository<T, I>,
	I extends Serializable> extends AbstractBaseSuperService<M, T, I> {

	protected static final int MAX_BATCH_KEY_SIZE = 20;

	@Autowired
	private CR classRepository;

	@Autowired
	private IR interfaceRepository;

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private HibernateDaoSupport hibernateDaoSupport;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public M im() {
		return super.getBaseMapper();
	}

	@Override
	public CR cr() {
		return classRepository;
	}

	@Override
	public IR ir() {
		return interfaceRepository;
	}

	@Override
	public JdbcTemplate jdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public HibernateTemplate hibernateTemplate() {
		return hibernateDaoSupport.getHibernateTemplate();
	}

	protected CacheKeyBuilder cacheKeyBuilder() {
		return () -> super.getEntityClass().getSimpleName();
	}

	@Override
	public void refreshCache() {
		list().forEach(this::setCache);
	}

	@Override
	public void clearCache() {
		list().forEach(this::delCache);
	}

	@Override
	public T getByIdCache(I id) {
		CacheKey cacheKey = cacheKeyBuilder().key(id);
		return redisRepository.get(cacheKey, k -> super.getById(id));
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public T getByKey(CacheKey key, Function<CacheKey, Object> loader) {
		Object id = redisRepository.get(key, loader);
		return id == null ? null : getByIdCache((I) Convert.toLong(id));
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> findByIds(@NonNull Collection<? extends Serializable> ids,
		Function<Collection<? extends Serializable>, Collection<T>> loader) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}
		// 拼接keys
		List<CacheKey> keys = ids.stream().map(cacheKeyBuilder()::key).collect(Collectors.toList());
		// 切割
		List<List<CacheKey>> partitionKeys = Lists.partition(keys, MAX_BATCH_KEY_SIZE);

		// 用切割后的 partitionKeys 分批去缓存查， 返回的是缓存中存在的数据
		List<T> valueList = partitionKeys.stream()
			.map(ks -> (List<T>) redisRepository.findByListCacheKey(ks))
			.flatMap(Collection::stream)
			.toList();

		// 所有的key
		List<Serializable> keysList = Lists.newArrayList(ids);
		// 缓存不存在的key
		Set<Serializable> missedKeys = Sets.newLinkedHashSet();

		List<T> allList = new ArrayList<>();
		for (int i = 0; i < valueList.size(); i++) {
			T v = valueList.get(i);
			Serializable k = keysList.get(i);
			if (v == null) {
				missedKeys.add(k);
			} else {
				allList.add(v);
			}
		}
		// 加载miss 的数据，并设置到缓存
		if (CollUtil.isNotEmpty(missedKeys)) {
			if (loader == null) {
				loader = this::listByIds;
			}
			Collection<T> missList = loader.apply(missedKeys);
			missList.forEach(this::setCache);
			allList.addAll(missList);
		}
		return allList;
	}

	/**
	 * 幂等性新增记录 例子如下：
	 * <p>
	 * String username = sysUser.getUsername();
	 * <p>
	 * boolean result = super.saveIdempotency( sysUser , lock , LOCK_KEY_USERNAME+username , new
	 * QueryWrapper<SysUser>().eq("username", username) );
	 *
	 * @param entity    实体对象
	 * @param lock      锁实例
	 * @param lockKey   锁的key
	 * @param predicate 判断是否存在的条件
	 * @param msg       对象已存在提示信息
	 */
	@Override
	public boolean saveIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate, Wrapper<T> countWrapper, String msg) {
		if (lock == null) {
			throw new LockException("分布式锁为空");
		}
		if (StrUtil.isEmpty(lockKey)) {
			throw new LockException("锁的key为空");
		}

		ZLock zLock = null;
		try {
			zLock = lock.lock(lockKey);
			if (Objects.nonNull(zLock)) {
				// 使用jpa判断
				if (Objects.nonNull(predicate)) {
					long count = classRepository.count(predicate);
					if (count == 0) {
						classRepository.save(entity);
						return true;
					} else {
						throw new IdempotencyException(StrUtil.isEmpty(msg) ? "数据已存在" : msg);
					}
				}

				//使用mybatis判断
				if (Objects.nonNull(countWrapper)) {
					//判断记录是否已存在
					long count = super.count(countWrapper);
					if (count == 0) {
						return super.save(entity);
					} else {
						throw new IdempotencyException(StrUtil.isEmpty(msg) ? "数据已存在" : msg);
					}
				}
			} else {
				throw new LockException("锁等待超时");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				lock.unlock(zLock);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * 幂等性新增记录
	 *
	 * @param entity    实体对象
	 * @param lock      锁实例
	 * @param lockKey   锁的key
	 * @param predicate 判断是否存在的条件
	 */
	@Override
	public boolean saveIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate, Wrapper<T> countWrapper) {
		return saveIdempotency(entity, lock, lockKey, predicate, countWrapper, null);
	}

	/**
	 * 幂等性新增或更新记录 例子如下： String username = sysUser.getUsername(); boolean result =
	 * super.saveOrUpdateIdempotency(sysUser, lock , LOCK_KEY_USERNAME+username , new
	 * QueryWrapper<SysUser>().eq("username", username));
	 *
	 * @param entity    实体对象
	 * @param lock      锁实例
	 * @param lockKey   锁的key
	 * @param predicate 判断是否存在的条件
	 * @param msg       对象已存在提示信息
	 */
	@Override
	public boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate, Wrapper<T> countWrapper, String msg) {
		if (null != entity) {
			Class<?> cls = entity.getClass();
			TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
			if (null != tableInfo && StrUtil.isNotEmpty(tableInfo.getKeyProperty())) {
				Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
				if (StringUtils.checkValNull(idVal) || Objects.isNull(
					getById((Serializable) idVal))) {
					return this.saveIdempotency(entity, lock, lockKey, predicate, countWrapper,
						StrUtil.isEmpty(msg) ? "数据已存在" : msg);
				} else {
					return updateById(entity);
				}
			} else {
				throw ExceptionUtils.mpe("执行错误,未找到@TableId.");
			}
		}
		return false;
	}

	/**
	 * 幂等性新增或更新记录 例子如下： String username = sysUser.getUsername(); boolean result =
	 * super.saveOrUpdateIdempotency(sysUser, lock , LOCK_KEY_USERNAME+username , new
	 * QueryWrapper<SysUser>().eq("username", username));
	 *
	 * @param entity    实体对象
	 * @param lock      锁实例
	 * @param lockKey   锁的key
	 * @param predicate 判断是否存在的条件
	 */
	@Override
	public boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate, Wrapper<T> countWrapper) {
		return saveOrUpdateIdempotency(entity, lock, lockKey, predicate, countWrapper, null);
	}

	protected void setCache(T model) {
		Object id = getId(model);
		if (id != null) {
			CacheKey key = cacheKeyBuilder().key(id);
			redisRepository.set(key, model);
		}
	}

	protected Object getId(T model) {
		if (model instanceof SuperEntity) {
			return ((SuperEntity) model).getId();
		} else {
			// 实体没有继承 Entity 和 SuperEntity
			TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
			if (tableInfo == null) {
				return null;
			}
			// 主键类型
			Class<?> keyType = tableInfo.getKeyType();
			if (keyType == null) {
				return null;
			}
			// id 字段名
			String keyProperty = tableInfo.getKeyProperty();

			// 反射得到 主键的值
			Field idField = ReflectUtil.getField(getEntityClass(), keyProperty);
			return ReflectUtil.getFieldValue(model, idField);
		}
	}

	protected void delCache(Serializable... ids) {
		delCache(Arrays.asList(ids));
	}

	protected void delCache(Collection<?> idList) {
		CacheKey[] keys = idList.stream().map(id -> cacheKeyBuilder().key(id))
			.toArray(CacheKey[]::new);
		redisRepository.del(keys);
	}

	protected void delCache(T model) {
		Object id = getId(model);
		if (id != null) {
			CacheKey key = cacheKeyBuilder().key(id);
			redisRepository.del(key);
		}
	}


	/**
	 * 默认批次提交数量
	 */
	public static final int DEFAULT_BATCH_SIZE = 1000;


	@Override
	public Class<T> getEntityClass() {
		return super.getEntityClass();
	}

	/**
	 * 获取主键明
	 */
	protected String getKeyProperty() {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
		Assert.notNull(tableInfo, "错误:无法执行.因为找不到实体的 TableInfo 缓存!");
		String keyProperty = tableInfo.getKeyProperty();
		Assert.notNull(keyProperty, "错误:无法执行.因为无法从实体中找到主键的列!");
		return keyProperty;
	}

	/*
	 * 以下的方法使用介绍:
	 *
	 * 一. 名称介绍
	 * 1. 方法名带有 query 的为对数据的查询操作, 方法名带有 update 的为对数据的修改操作
	 * 2. 方法名带有 lambda 的为内部方法入参 column 支持函数式的
	 * 二. 支持介绍
	 *
	 * 1. 方法名带有 query 的支持以 {@link ChainQuery} 内部的方法名结尾进行数据查询操作
	 * 2. 方法名带有 update 的支持以 {@link ChainUpdate} 内部的方法名为结尾进行数据修改操作
	 *
	 * 三. 使用示例,只用不带 lambda 的方法各展示一个例子,其他类推
	 * 1. 根据条件获取一条数据: `query().eq("column", value).one()`
	 * 2. 根据条件删除一条数据: `update().eq("column", value).remove()`
	 *
	 */

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<T> saveAll(List<T> list) {
		if (CollUtil.isNotEmpty(list)) {
			saveBatch(list, DEFAULT_BATCH_SIZE);
		}
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateAllById(Collection<T> entityList) {
		return updateBatchById(entityList, DEFAULT_BATCH_SIZE);
	}

	@Override
	public boolean updateByField(T t, SFunction<T, ?> field, Object fieldValue) {
		return lambdaUpdate()
			.eq(field, fieldValue)
			.update(t);
	}

	@Override
	public List<T> findAll() {
		return lambdaQuery().list();
	}


	@Override
	public Optional<T> findById(Serializable id) {
		return Optional.ofNullable(baseMapper.selectById(id));
	}


	@Override
	public Optional<T> findByField(SFunction<T, ?> field, Object fieldValue) {
		return lambdaQuery().eq(field, fieldValue).oneOpt();
	}

	@Override
	public List<T> findAllByIds(Collection<? extends Serializable> idList) {
		if (CollUtil.isEmpty(idList)) {
			return new ArrayList<>(0);
		}
		return baseMapper.selectBatchIds(idList);
	}

	@Override
	public List<T> findAllByField(SFunction<T, ?> field, Object fieldValue) {
		return lambdaQuery().eq(field, fieldValue).list();
	}

	@Override
	public List<T> findAllByFields(SFunction<T, ?> field,
		Collection<? extends Serializable> fieldValues) {
		if (CollUtil.isEmpty(fieldValues)) {
			return new ArrayList<>(0);
		}
		return lambdaQuery().in(field, fieldValues).list();
	}

	@Override
	public boolean existedById(Serializable id) {
		String keyProperty = this.getKeyProperty();
		return query().eq(keyProperty, id).exists();
	}

	@Override
	public boolean existedByField(SFunction<T, ?> field, Object fieldValue) {
		return lambdaQuery().eq(field, fieldValue).exists();
	}

	@Override
	public boolean existedByField(SFunction<T, ?> field, Object fieldValue, Serializable id) {
		String keyProperty = this.getKeyProperty();
		return query().eq(getColumnName(field), fieldValue)
			.ne(keyProperty, id)
			.exists();
	}

	@Override
	public Long countByField(SFunction<T, ?> field, Object fieldValue) {
		return lambdaQuery().eq(field, fieldValue).count();
	}

	@Override
	public boolean deleteById(Serializable id) {
		return SqlHelper.retBool(baseMapper.deleteById(id));
	}

	@Override
	public boolean deleteByIds(Collection<? extends Serializable> idList) {
		if (CollUtil.isNotEmpty(idList)) {
			return SqlHelper.retBool(baseMapper.deleteBatchIds(idList));
		}
		return false;
	}

	@Override
	public boolean deleteByField(SFunction<T, ?> field, Object fieldValue) {
		return lambdaUpdate().eq(field, fieldValue).remove();
	}

	@Override
	public boolean deleteByFields(SFunction<T, ?> field, Collection<?> fieldValues) {
		if (CollUtil.isEmpty(fieldValues)) {
			return false;
		}
		return lambdaUpdate().in(field, fieldValues).remove();
	}

	@Override
	public String getColumnName(SFunction<T, ?> function) {
		LambdaMeta meta = LambdaUtils.extract(function);
		Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(meta.getInstantiatedClass());
		Assert.notEmpty(columnMap, "错误:无法执行.因为无法获取到实体类的表对应缓存!");
		String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
		ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(fieldName));
		return columnCache.getColumn();
	}
}
