package com.ecust.auth.mapper;

import com.ecust.base.entity.Role;
import com.ecust.base.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from library.user where phone = #{phone}")
    User getUserByPhone(String phone);


    void addUser(User user);

    @Select("select * from library.user where username=#{username}")
    User getUserByName(String username);

    @Select("select * from library.user")
    Page<User> getAll();


    @Update("update library.user set phone = #{phone} where id = #{id}")
    void updateUser(User user);



    @Delete("delete from library.user where id=#{id}")
    void deleteUser(Long id);

}
