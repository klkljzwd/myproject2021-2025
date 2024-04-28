package com.ecust.base.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (RoleUser)表实体类
 *
 * @author makejava
 * @since 2024-03-06 21:53:33
 */
@SuppressWarnings("serial")
@Data
public class RoleUser{
    //id
    private Long id;
    //用户id
    private Long userId;
    //角色id
    private Long roleId;

}

