package com.taotao.cloud.elk.helper;

import com.github.dozermapper.core.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DozerHelper
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:24:48
 */
public class DozerHelper {

	private final Mapper mapper;

	public DozerHelper(Mapper mapper) {
		this.mapper = mapper;
	}

	public Mapper getMapper() {
		return this.mapper;
	}

	/**
	 * map
	 *
	 * @param source           数据源
	 * @param destinationClass 目标类
	 * @param <T>              T
	 * @return 对象
	 * @since 2021-09-02 21:24:52
	 */
	public <T> T map(Object source, Class<T> destinationClass) {
		if (source == null) {
			return null;
		}
		return mapper.map(source, destinationClass);
	}

	/**
	 * map2
	 *
	 * @param source           数据源
	 * @param destinationClass 目标类
	 * @return 对象
	 * @since 2021-09-02 21:25:04
	 */
	public <T> T map2(Object source, Class<T> destinationClass) {
		if (source == null) {
			try {
				return destinationClass.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
			}
		}
		return mapper.map(source, destinationClass);
	}

	/**
	 * map
	 *
	 * @param source      数据源
	 * @param destination 目标类
	 * @since 2021-09-02 21:25:12
	 */
	public void map(Object source, Object destination) {
		if (source == null) {
			return;
		}
		mapper.map(source, destination);
	}

	/**
	 * map
	 *
	 * @param source           数据源
	 * @param destinationClass 目标类
	 * @param mapId            mapId
	 * @return 结果对象
	 * @since 2021-09-02 21:25:15
	 */
	public <T> T map(Object source, Class<T> destinationClass, String mapId) {
		if (source == null) {
			return null;
		}
		return mapper.map(source, destinationClass, mapId);
	}

	/**
	 * map
	 *
	 * @param source      数据源
	 * @param destination 目标类
	 * @param mapId       mapId
	 * @since 2021-09-02 21:25:21
	 */
	public void map(Object source, Object destination, String mapId) {
		if (source == null) {
			return;
		}
		mapper.map(source, destination, mapId);
	}

	/**
	 * mapList
	 *
	 * @param sourceList       数据源
	 * @param destinationClass 目标类
	 * @return 数据集合
	 * @since 2021-09-02 21:25:26
	 */
	public <T, E> List<T> mapList(Collection<E> sourceList, Class<T> destinationClass) {
		return mapPage(sourceList, destinationClass);
	}

	/**
	 * mapPage
	 *
	 * @param sourceList       数据源
	 * @param destinationClass 目标类
	 * @return 分页结果
	 * @since 2021-09-02 21:25:36
	 */
	public <T, E> List<T> mapPage(Collection<E> sourceList, Class<T> destinationClass) {
		if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
			return Collections.emptyList();
		}

		return sourceList.parallelStream()
			.filter(Objects::nonNull)
			.map((sourceObject) -> mapper.map(sourceObject, destinationClass))
			.collect(Collectors.toList());
	}

	/**
	 * mapSet
	 *
	 * @param sourceList       数据源
	 * @param destinationClass 目标类
	 * @return 数据集合
	 * @since 2021-09-02 21:25:56
	 */
	public <T, E> Set<T> mapSet(Collection<E> sourceList, Class<T> destinationClass) {
		if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
			return Collections.emptySet();
		}
		return sourceList.parallelStream()
			.map((sourceObject) -> mapper.map(sourceObject, destinationClass))
			.collect(Collectors.toSet());
	}
}
