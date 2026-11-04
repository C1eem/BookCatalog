package com.example.book_catalog.controller;

import com.example.book_catalog.dto.BookRequest;
import com.example.book_catalog.dto.BookResponse;
import com.example.book_catalog.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.book_catalog.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        return bookService.create(request);
    }

    @GetMapping("/{id}")
    public BookResponse findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping
    public PageResponse<BookResponse> findAll(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            // page по умолчанию 0, size = 10, сортировка по title по возрастанию
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        
        return bookService.findAll(authorId, genre, yearFrom, yearTo, pageable);
    }
}