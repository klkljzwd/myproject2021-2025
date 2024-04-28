package com.ecust.auth.controller;

import com.ecust.auth.service.AuthorityService;
import com.ecust.base.ResponseResult;
import com.ecust.base.entity.Authority;
import com.ecust.base.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin
public class AuthorityController {
    @Autowired
    private AuthorityService authorityService;

    @GetMapping
    public ResponseResult getAuthList(){
        return authorityService.getList();
    }

    @PostMapping(value = "/update")
    public ResponseResult updateAuth(@RequestBody Authority authority){
        return authorityService.updateAuth(authority);
    }
    @PostMapping(value = "/add")
    public ResponseResult addAuth(@RequestBody Authority authority){
        return authorityService.addAuth(authority);
    }
    @GetMapping(value = "/delete")
    public ResponseResult deleteAuth(@RequestParam Long id){
        return authorityService.deleteAuth(id);
    }
}
