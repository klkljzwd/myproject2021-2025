package com.ecust.base.entity;


import lombok.Data;

/**
 * (Books)表实体类
 *
 * @author makejava
 * @since 2024-03-12 21:47:29
 */
@SuppressWarnings("serial")
@Data
public class Books{
    //id
    private Long id;
    //书名
    private String name;
    //作者
    private String author;
    //是否vip专享
    private Integer isVip;
    //描述
    private String description;
    //出版社
    private String publish;
    //书类别
    private String type;

}

