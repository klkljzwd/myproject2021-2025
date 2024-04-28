package com.ecust.base.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2024-03-06 21:53:33
 */
@SuppressWarnings("serial")
@Data
public class User{
    //用户id
    private Long id;
    //用户名
    private String username;
    //手机号
    private String phone;
    //密码
    private String password;
    //用户状态 0-禁止 1-正常
    private Integer status;

}

