package com.taotao.cloud.common.execl.temp.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.taotao.cloud.common.utils.log.LogUtil;

/**
 * TODO
 *

 * @date 2020/4/9 16:33
 */

public class TestListener extends AnalysisEventListener {

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        LogUtil.info("解析一条:{}", JSON.toJSONString(o));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
