package com.ecust.base.vo;

import com.ecust.base.entity.Authority;
import lombok.Data;

import java.util.List;

@Data
public class RoleAuthBindVO {
    private List<Authority> authoritiesNot;
    private List<Authority> authorities;
}
