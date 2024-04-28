package com.ecust.auth.mapper;

import com.ecust.base.entity.RoleAuthority;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleAuthorityMapper {
    List<Long> getRoleAuthority(List<Long> ids);

    @Select("select * from library.role_authority where role_id=#{roleId}")
    List<RoleAuthority> getRoleAuthByRoleId(Long roleId);

    @Select("select * from library.role_authority where auth_id=#{id}")
    List<RoleAuthority> getRoleAuthByAuthId(Long id);

    @Delete("delete from library.role_authority where auth_id=#{authId} and role_id=#{roleId}")
    void deleteRoleAuth(Long roleId,Long authId);

    @Insert("insert into library.role_authority values (null,#{roleId},#{authId})")
    void addRoleAuth(Long roleId,Long authId);
}
