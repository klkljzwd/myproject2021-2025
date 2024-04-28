package com.ecust.auth.mapper;

import com.ecust.base.entity.RoleUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    @Select("select role_id from library.role_user where user_id=#{id}")
    List<Long> getRoleUser(Long id);

    @Insert("insert into library.role_user values (null,#{userId},#{roleId})")
    void addUserRole(RoleUser roleUser);

    @Select("select * from library.role_user where role_id=#{roleId}")
    List<RoleUser> getUserIdByRole(Long roleId);

    @Update("update library.role_user set role_id = #{roleId} where user_id=#{userId}")
    void registerReader(Long roleId,Long userId);
}
