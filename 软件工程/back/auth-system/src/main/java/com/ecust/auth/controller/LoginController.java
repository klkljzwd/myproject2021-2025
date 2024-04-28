package com.ecust.auth.controller;

import com.ecust.auth.service.LoginService;
import com.ecust.base.ResponseResult;
import com.ecust.base.constant.Constant;
import com.ecust.base.dto.AdminLoginDTO;
import com.ecust.base.dto.LoginDTO;
import com.ecust.base.dto.LoginPhoneDTO;
import com.ecust.base.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping(value = "/api/login")
@CrossOrigin
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseResult<LoginVo> login(@RequestBody LoginDTO loginDTO){
        ResponseResult<LoginVo> login = loginService.login(loginDTO);
        if(login==null){
            ResponseResult responseResult = new ResponseResult();
            responseResult.setCode(401);
            return responseResult;
        }
        return login;
    }

    @PostMapping("/admin")
    public ResponseResult adminLogin(@RequestBody AdminLoginDTO adminLoginDTO){
        return loginService.adminLogin(adminLoginDTO);
    }
    @PostMapping("/register")
    public ResponseResult userRegister(@RequestBody LoginDTO loginDTO){
        ResponseResult responseResult = loginService.userRegister(loginDTO);
        if(responseResult==null){
            ResponseResult response = new ResponseResult();
            responseResult.setCode(401);
            return responseResult;
        }
        return responseResult;
    }

}
