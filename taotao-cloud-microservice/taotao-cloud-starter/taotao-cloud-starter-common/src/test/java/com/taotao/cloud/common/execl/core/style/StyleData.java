package com.taotao.cloud.common.execl.core.style;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;



/**

 */
com.taotao.cloud.common.execl
@HeadStyle
@HeadFontStyle
public class StyleData {
    @ExcelProperty("字符串")
    private String string;
    @ExcelProperty("字符串1")
    private String string1;
}
