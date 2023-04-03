package com.taotao.cloud.log.biz.log.core.mongo.dao;

import com.taotao.cloud.log.biz.log.core.mongo.entity.OperateLogMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
* mongo持久化方式
* @author shuigedeng  
* @date 2021/12/2 
*/
public interface OperateLogMongoRepository extends MongoRepository<OperateLogMongo,Long> {
}
