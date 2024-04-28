package com.ecust.auth.controller;

import com.ecust.auth.service.UserService;
import com.ecust.base.PageResult;
import com.ecust.base.ResponseResult;
import com.ecust.base.dto.PageUserDTO;
import com.ecust.base.dto.UserRegister;
import com.ecust.base.entity.Role;
import com.ecust.base.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 管理员增删改查
     * */
    @PostMapping(value = "/update")
    public ResponseResult updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }
    @PostMapping(value = "/add")
    public ResponseResult addUser(@RequestBody User user){
        return userService.addUser(user);
    }
    @GetMapping(value = "/delete")
    public ResponseResult deleteUser(@RequestParam Long id){
        return userService.deleteUser(id);
    }

    @GetMapping(value = "/page")
    public PageResult queryUser(@RequestParam Long currentPage,
                                @RequestParam Long pageSize){
        return userService.queryUser(currentPage,pageSize);
    }
    /**
     * 会员注册
     */
    @PostMapping(value = "/register")
    public ResponseResult registerUser(@RequestBody UserRegister userRegister){
        return userService.registerUser(userRegister);
    }
}
