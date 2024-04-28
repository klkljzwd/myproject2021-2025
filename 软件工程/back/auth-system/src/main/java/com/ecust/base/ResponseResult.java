package com.ecust.base;

import lombok.Data;

@Data
public class ResponseResult<T>{
    private T data;
    private Integer code;
    private String msg;
}
