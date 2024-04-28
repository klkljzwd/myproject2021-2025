package com.ecust.base.dto;

import com.ecust.base.entity.User;
import lombok.Data;

@Data
public class PageUserDTO {
    private Long page;
    private Long pageSize;
}
