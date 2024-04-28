package com.ecust.auth.service;

import com.ecust.base.ResponseResult;
import com.ecust.base.entity.Authority;

public interface AuthorityService {
    ResponseResult getList();

    ResponseResult updateAuth(Authority authority);

    ResponseResult addAuth(Authority authority);

    ResponseResult deleteAuth(Long id);
}
