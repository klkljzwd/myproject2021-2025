package com.ecust.auth.service.impl;

import com.ecust.auth.advicer.NormalException;
import com.ecust.auth.mapper.AuthorityMapper;
import com.ecust.auth.mapper.RoleAuthorityMapper;
import com.ecust.auth.service.AuthorityService;
import com.ecust.base.ResponseResult;
import com.ecust.base.annotation.AuthPermission;
import com.ecust.base.entity.Authority;
import com.ecust.base.entity.RoleAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;

    @Override
    @AuthPermission(level = 0)
    public ResponseResult getList() {
        List<Authority> list = authorityMapper.getList();
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(list);
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult updateAuth(Authority authority) {
        authorityMapper.updateAuth(authority);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult addAuth(Authority authority) {
        authorityMapper.addAuth(authority);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult deleteAuth(Long id) {
        List<RoleAuthority> roleAuthByAuthId = roleAuthorityMapper.getRoleAuthByAuthId(id);
        if(roleAuthByAuthId.size()!=0){
            throw new NormalException("该权限和角色绑定，无法删除");
        }
        authorityMapper.deleteAuth(id);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }
}
