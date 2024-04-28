package com.ecust.auth.service;

import com.ecust.base.ResponseResult;
import com.ecust.base.dto.RoleDTO;
import com.ecust.base.entity.Role;
import com.ecust.base.vo.RoleAuthVO;

import java.util.List;

public interface RoleManageService {
    ResponseResult<List<Role>> getAll();

    ResponseResult updateRole(Role role);

    ResponseResult addRole(Role role);

    ResponseResult deleteRole(Long id);

    ResponseResult queryBindAuth(Long id);

    ResponseResult deleteRoleAuth(Long roleId,Long authId);

    ResponseResult addRoleAuth(Long roleId, Long authId);
}
