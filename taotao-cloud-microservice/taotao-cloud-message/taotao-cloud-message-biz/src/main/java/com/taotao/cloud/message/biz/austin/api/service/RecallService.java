package com.taotao.cloud.message.biz.austin.api.service;

import com.taotao.cloud.message.biz.austin.service.api.domain.SendRequest;
import com.taotao.cloud.message.biz.austin.service.api.domain.SendResponse;

/**
 * 撤回接口
 *
 * @author 3y
 */
public interface RecallService {


    /**
     * 根据模板ID撤回消息
     *
     * @param sendRequest
     * @return
     */
    SendResponse recall(SendRequest sendRequest);
}
