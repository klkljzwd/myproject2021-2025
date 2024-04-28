package com.ecust.auth.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBookMapper {
    @Insert("insert into library.user_book values (null,#{userId},#{bookId})")
    void addUserBook(Long userId,Long bookId);
}
