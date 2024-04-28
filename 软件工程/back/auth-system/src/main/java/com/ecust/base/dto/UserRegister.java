package com.ecust.base.dto;

import lombok.Data;

@Data
public class UserRegister {
    private String username;
    private String phone;
    private Boolean isVip;
}
