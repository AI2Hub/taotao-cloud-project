package com.taotao.cloud.rpc.common.common.support.resource.impl;

import com.taotao.cloud.rpc.common.common.api.Destroyable;
import com.taotao.cloud.rpc.common.common.support.resource.ResourceManager;

import java.util.List;

/**
 * <p> project: rpc-DefaultResourceManager </p>
 * <p> create on 2019/10/30 21:28 </p>
 *
 * @author Administrator
 * @since 0.1.3
 */
@ThreadSafe
public class DefaultResourceManager implements ResourceManager {

    /**
     * DefaultResourceManager logger
     */
    private static final Log LOG = LogFactory.getLog(DefaultResourceManager.class);

    /**
     * 可销毁的列表
     * @since 0.1.3
     */
    private List<Destroyable> destroyableList = Guavas.newArrayList();

    @Override
    public synchronized ResourceManager addDestroy(Destroyable destroyable) {
        LOG.info("[Resource] add destroyable: {}", destroyable);
        destroyableList.add(destroyable);
        return this;
    }

    @Override
    public synchronized ResourceManager destroyAll() {
        LOG.info("[Resource] destroyableList.size(): {}", destroyableList.size());

        // 依次销毁
        for(Destroyable destroyable : destroyableList) {
            LOG.info("[Resource] destroy destroyable: {}", destroyable);
            destroyable.destroy();
        }

        // 清空列表
        LOG.info("[Resource] clear destroyableList");
        this.destroyableList = Guavas.newArrayList();
        return this;
    }

}
