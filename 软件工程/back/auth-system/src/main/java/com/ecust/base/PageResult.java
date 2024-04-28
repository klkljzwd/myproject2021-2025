package com.ecust.base;

import lombok.Data;

@Data
public class PageResult {
    private Long total;
    private Long page;
    private ResponseResult responseResult;
}
