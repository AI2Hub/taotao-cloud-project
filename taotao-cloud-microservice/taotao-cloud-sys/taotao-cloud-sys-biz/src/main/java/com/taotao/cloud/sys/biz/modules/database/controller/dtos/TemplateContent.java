package com.taotao.cloud.sys.biz.modules.database.controller.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TemplateContent {
    @NotNull
    private String name;
    @NotNull
    private String content;
}
