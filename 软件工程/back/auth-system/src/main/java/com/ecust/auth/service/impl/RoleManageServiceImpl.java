package com.ecust.auth.service.impl;

import com.ecust.auth.advicer.NormalException;
import com.ecust.auth.mapper.AuthorityMapper;
import com.ecust.auth.mapper.RoleAuthorityMapper;
import com.ecust.auth.mapper.RoleMapper;
import com.ecust.auth.mapper.UserRoleMapper;
import com.ecust.auth.service.RoleManageService;
import com.ecust.base.ResponseResult;
import com.ecust.base.ThreadLocalAuth;
import com.ecust.base.annotation.AuthPermission;
import com.ecust.base.dto.RoleDTO;
import com.ecust.base.entity.Authority;
import com.ecust.base.entity.Role;
import com.ecust.base.entity.RoleAuthority;
import com.ecust.base.entity.RoleUser;
import com.ecust.base.vo.RoleAuthBindVO;
import com.ecust.base.vo.RoleAuthVO;
import com.ecust.utils.FlushUser;
import com.ecust.utils.RedisAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleManageServiceImpl implements RoleManageService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private AuthorityMapper authorityMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @AuthPermission(level = 0)
    public ResponseResult<List<Role>> getAll() {
        List<Role> roles = roleMapper.getRoles();
        ResponseResult<List<Role>> responseResult = new ResponseResult<>();
        responseResult.setCode(200);
        responseResult.setData(roles);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult updateRole(Role role) {

        roleMapper.updateRole(role);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult addRole(Role role) {
        if(role.getRoleName()==null || role.getDescription()==null){
            throw new NormalException("参数错误");
        }
        roleMapper.addRole(role);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult deleteRole(Long id) {

        //查询相关关联的roleUser和roleAuth
        List<RoleUser> userIdByRole = userRoleMapper.getUserIdByRole(id);
        if(userIdByRole.size()!=0){
            throw new NormalException("该角色有先关联的用户，无法删除");
        }
        //查询关联权限
        List<RoleAuthority> roleAuthByRoleId = roleAuthorityMapper.getRoleAuthByRoleId(id);
        if(roleAuthByRoleId.size()!=0){
            throw new NormalException("该角色有先关联的权限，无法删除");
        }
        roleMapper.deleteRole(id);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult queryBindAuth(Long id) {
        //根据角色id查询绑定的权限
        List<RoleAuthority> roleAuthByRoleId = roleAuthorityMapper.getRoleAuthByRoleId(id);
        if(roleAuthByRoleId==null||roleAuthByRoleId.size()==0){
            ResponseResult responseResult = new ResponseResult();
            responseResult.setCode(405);
            responseResult.setMsg("该角色没有权限");
            return responseResult;
        }
        //根据权限id查询对应的权限
        List<Long> authIds = new ArrayList<>();
        for(RoleAuthority roleAuthority:roleAuthByRoleId){
            authIds.add(roleAuthority.getAuthId());
        }
        //查询已有的权限
        List<Authority> authority = authorityMapper.getAuthority(authIds);
        //查询没有绑定的权限
        List<Authority> authorityNot = authorityMapper.getAuthorityNot(authIds);
        RoleAuthBindVO authBindVO = new RoleAuthBindVO();
        authBindVO.setAuthorities(authority);
        authBindVO.setAuthoritiesNot(authorityNot);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(authBindVO);
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult deleteRoleAuth(Long roleId, Long authId) {
        roleAuthorityMapper.deleteRoleAuth(roleId,authId);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        FlushUser.flush(stringRedisTemplate);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 0)
    public ResponseResult addRoleAuth(Long roleId, Long authId) {
        roleAuthorityMapper.addRoleAuth(roleId,authId);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        FlushUser.flush(stringRedisTemplate);
        return responseResult;
    }

}
