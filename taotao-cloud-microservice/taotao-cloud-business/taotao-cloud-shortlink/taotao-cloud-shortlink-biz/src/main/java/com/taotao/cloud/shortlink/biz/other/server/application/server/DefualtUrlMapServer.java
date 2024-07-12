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

package com.taotao.cloud.shortlink.biz.other.server.application.server;

import com.taotao.cloud.shortlink.biz.other.server.application.dto.UrlRequest;
import com.taotao.cloud.shortlink.biz.other.server.application.dto.UrlResponse;
import com.taotao.cloud.shortlink.biz.other.server.application.handler.StatisticsHandler;
import com.taotao.cloud.shortlink.biz.other.server.application.handler.UrlMapHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefualtUrlMapServer implements UrlMapServer {

    private static final String LONG2SHORTURL = "长链接转短连接";
    private static final String SHORT2LONGURL = "根据短链接获取长链接";

    @Autowired
    UrlMapHandler urlMaphandler;

    @Autowired
    StatisticsHandler statisticsHandler;

    @Override
    public ResponseEntity getShortUrl(UrlRequest longUrlReq) {
        // 1.先在内存中查询关键字longUrl是否存在
        log.info("{} long Url {}", longUrlReq.getRequestId(), longUrlReq.getLongUrl());
        UrlResponse resp = new UrlResponse();
        resp.setDescription(LONG2SHORTURL);
        urlMaphandler.handle(longUrlReq, resp);
        statisticsHandler.handle(longUrlReq, resp);
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity getLongUrl(UrlRequest shortUrlReq) {
        log.info("{} short request {}", shortUrlReq.getRequestId(), shortUrlReq.getShortUrl());
        UrlResponse resp = new UrlResponse();
        resp.setDescription(SHORT2LONGURL);
        urlMaphandler.handle(shortUrlReq, resp);
        statisticsHandler.handle(shortUrlReq, resp);
        return ResponseEntity.ok(resp);
    }
}
