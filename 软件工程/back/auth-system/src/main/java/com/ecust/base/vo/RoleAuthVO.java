package com.ecust.base.vo;

import com.ecust.base.entity.Authority;
import lombok.Data;

import java.util.List;

@Data
public class RoleAuthVO {
    private String roleName;

    private String description;

    private List<Authority> authorities;
}
