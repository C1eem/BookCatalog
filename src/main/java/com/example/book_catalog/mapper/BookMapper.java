package com.example.book_catalog.mapper;

import com.example.book_catalog.dto.BookRequest;
import com.example.book_catalog.dto.BookResponse;
import com.example.book_catalog.entity.Author;
import com.example.book_catalog.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest request, Author author) {
        Book book = new Book();
        updateEntity(book, request, author);
        return book;
    }

    public void updateEntity(Book book, BookRequest request, Author author) {
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setPublicationYear(request.publicationYear());
        book.setGenre(request.genre());
        book.setPrice(request.price());
        book.setAuthor(author);
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getGenre(),
                book.getPrice(),
                book.getAuthor().getId(),
                book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName(),
                book.getCreatedAt()
        );
    }
}