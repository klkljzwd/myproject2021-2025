package com.ecust.auth.service;

import com.ecust.base.PageResult;
import com.ecust.base.ResponseResult;
import com.ecust.base.dto.UserRegister;
import com.ecust.base.entity.Role;
import com.ecust.base.entity.User;
import com.github.pagehelper.Page;

public interface UserService {

    ResponseResult updateUser(User user);

    ResponseResult addUser(User user);

    ResponseResult deleteUser(Long id);

    PageResult queryUser(Long currentPage, Long pageSize);

    ResponseResult registerUser(UserRegister userRegister);
}
