package com.ecust.auth.mapper;

import com.ecust.base.entity.Authority;
import com.ecust.base.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AuthorityMapper {
    //查询角色存在的权限
    List<Authority> getAuthority(List<Long> ids);
    //查询角色没有的权限
    List<Authority> getAuthorityNot(List<Long> ids);

    @Select("select * from library.authority where resource=#{name}")
    List<Authority> getAdminAuth(String name);

    @Select("select * from library.authority")
    List<Authority> getList();


    @Update("update library.authority set resource=#{resource},status=#{status},level=#{level} where id=#{id}")
    void updateAuth(Authority authority);

    @Insert("insert into library.authority values (null,#{resource},#{status},#{level})")
    void addAuth(Authority authority);

    @Delete("delete from library.authority where id=#{id}")
    void deleteAuth(Long id);


}
