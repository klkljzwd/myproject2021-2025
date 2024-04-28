package com.ecust.base.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * (Authority)表实体类
 *
 * @author makejava
 * @since 2024-03-06 21:53:32
 */
@SuppressWarnings("serial")
@Data
public class Authority{
    //权限id
    private Long id;
    //资源
    private String resource;
    //权限状态 0-无效 1-有效
    private Integer status;
    //权限层级
    private Integer level;

}

