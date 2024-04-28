package com.ecust.base.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * (Role)表实体类
 *
 * @author makejava
 * @since 2024-03-06 21:53:33
 */
@SuppressWarnings("serial")
@Data
public class Role{
    //角色id
    private Long id;
    //角色名
    private String roleName;
    private String description;

}

