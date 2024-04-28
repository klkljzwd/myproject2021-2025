package com.ecust.base.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * (RoleAuthority)表实体类
 *
 * @author makejava
 * @since 2024-03-06 21:53:33
 */
@SuppressWarnings("serial")
@Data
public class RoleAuthority{
    //id
    private Long id;
    //角色id
    private Long roleId;
    //权限id
    private Long authId;

}

