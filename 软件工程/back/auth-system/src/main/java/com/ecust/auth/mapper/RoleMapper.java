package com.ecust.auth.mapper;

import com.ecust.base.dto.RoleDTO;
import com.ecust.base.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("select id,role_name as roleName,description from library.role")
    List<Role> getRoles();

    @Update("update library.role set role_name = #{roleName} , description=#{description} where id=#{id}")
    void updateRole(Role role);

    @Insert("insert into library.role values (null,#{roleName},#{description})")
    void addRole(Role role);

    @Delete("delete from library.role where id=#{id}")
    void deleteRole(Long id);

}
