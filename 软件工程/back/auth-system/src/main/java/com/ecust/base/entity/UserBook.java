package com.ecust.base.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (UserBook)表实体类
 *
 * @author makejava
 * @since 2024-03-13 10:37:18
 */
@SuppressWarnings("serial")
@Data
public class UserBook{
    
    private Long id;
    
    private Long userId;
    
    private Long bookId;

}

