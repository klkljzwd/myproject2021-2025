package com.ecust.book.service;

import com.ecust.base.PageResult;
import com.ecust.base.ResponseResult;
import com.ecust.base.dto.SearchBooksDTO;
import com.ecust.base.entity.Books;

import java.awt.print.Book;

public interface BooksService {
    PageResult searchBook(SearchBooksDTO searchBooksDTO);

    ResponseResult lendBook(Long id);

    ResponseResult addBook(Books book);

    ResponseResult updateBook(Books books);

    ResponseResult deleteBook(Long id);
}
