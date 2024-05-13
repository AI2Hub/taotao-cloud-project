package com.taotao.cloud.rpc.registry.register.api.config;

/**
 * 注册配置接口信息
 * @author shuigedeng
 * @since 0.0.8
 */
public interface RegisterConfig {

    /**
     * 服务端口号
     * @param port 端口号
     * @return this
     * @since 0.0.8
     */
    RegisterConfig port(final int port);

    /**
     * 启动服务
     * @return this
     * @since 0.0.8
     */
    RegisterConfig start();

}
