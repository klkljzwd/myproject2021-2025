package com.ecust.auth.controller;

import com.ecust.auth.service.RoleManageService;
import com.ecust.base.ResponseResult;
import com.ecust.base.dto.RoleDTO;
import com.ecust.base.entity.Role;
import com.ecust.base.vo.RoleAuthVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/role")
@CrossOrigin
public class RoleManageController {
    @Autowired
    private RoleManageService roleManageService;
    @GetMapping
    public ResponseResult<List<Role>> getAll(){
        return roleManageService.getAll();
    }
    @PostMapping(value = "/update")
    public ResponseResult updateRole(@RequestBody Role role){
        return roleManageService.updateRole(role);
    }
    @PostMapping(value = "/add")
    public ResponseResult addRole(@RequestBody Role role){
        return roleManageService.addRole(role);
    }
    @GetMapping(value = "/delete")
    public ResponseResult deleteRole(@RequestParam Long id){
        return roleManageService.deleteRole(id);
    }

    @GetMapping(value = "/bind")
    public ResponseResult queryBindAuth(@RequestParam Long id){
        return roleManageService.queryBindAuth(id);
    }

    @GetMapping(value = "/deleteAuth")
    public ResponseResult deleteRoleAuth(@RequestParam Long roleId,@RequestParam Long authId){
        return roleManageService.deleteRoleAuth(roleId,authId);
    }
    @GetMapping(value = "/addAuth")
    public ResponseResult addRoleAuth(@RequestParam Long roleId,@RequestParam Long authId){
        return roleManageService.addRoleAuth(roleId,authId);
    }
}
