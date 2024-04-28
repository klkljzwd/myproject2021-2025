package com.ecust.auth.service;

import com.ecust.base.ResponseResult;
import com.ecust.base.dto.AdminLoginDTO;
import com.ecust.base.dto.LoginDTO;
import com.ecust.base.dto.LoginPhoneDTO;
import com.ecust.base.vo.LoginVo;

public interface LoginService {

    /**
     * 登录
     * */
    ResponseResult<LoginVo> login(LoginDTO loginDTO);

    /**
     * 管理员登录
     * */
    ResponseResult adminLogin(AdminLoginDTO adminLoginDTO);
    /**
     * 用户注册
     * */
    ResponseResult userRegister(LoginDTO loginDTO);
}
