package com.ecust.book.mapper;

import com.ecust.base.entity.Books;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BooksMapper {
    @Select("select * from library.books")
    List<Books> queryAllBooks();

    @Select("select * from library.books where id=#{id}")
    Books getBookById(Long id);

    void addBook(Books books);

    @Update("update library.books set name=#{name},author=#{author},is_vip=#{isVip},description=#{description},publish=#{publish},type=#{type} where id = #{id}")
    void updateBook(Books books);

    @Delete("delete from library.books where id = #{id}")
    void deleteBook(Long id);


}
