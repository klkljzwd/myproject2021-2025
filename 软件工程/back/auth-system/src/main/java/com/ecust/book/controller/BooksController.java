package com.ecust.book.controller;

import com.ecust.base.PageResult;
import com.ecust.base.ResponseResult;
import com.ecust.base.dto.SearchBooksDTO;
import com.ecust.base.entity.Books;
import com.ecust.book.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/book")
@CrossOrigin
public class BooksController {
    @Autowired
    private BooksService booksService;

    @PostMapping
    public PageResult searchBook(@RequestBody SearchBooksDTO searchBooksDTO){
        return booksService.searchBook(searchBooksDTO);
    }

    @GetMapping(value = "/read")
    public ResponseResult lendBook(@RequestParam Long id){
        return booksService.lendBook(id);
    }

    @PostMapping(value = "/addBook")
    public ResponseResult addBook(@RequestBody  Books book){
        return booksService.addBook(book);
    }

    @PostMapping(value = "/updateBook")
    public ResponseResult updateBook(@RequestBody Books books){
        return booksService.updateBook(books);
    }

    @GetMapping(value = "/deleteBook")
    public ResponseResult deleteBook(@RequestParam Long id){
        return booksService.deleteBook(id);
    }
}
