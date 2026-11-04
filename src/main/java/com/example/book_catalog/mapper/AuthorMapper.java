package com.example.book_catalog.mapper;

import com.example.book_catalog.dto.AuthorRequest;
import com.example.book_catalog.dto.AuthorResponse;
import com.example.book_catalog.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public Author toEntity(AuthorRequest request) {
        Author author = new Author();
        author.setFirstName(request.firstName());
        author.setLastName(request.lastName());
        author.setBirthDate(request.birthDate());
        author.setNationality(request.nationality());
        return author;
    }

    public void updateEntity(Author author, AuthorRequest request) {
        author.setFirstName(request.firstName());
        author.setLastName(request.lastName());
        author.setBirthDate(request.birthDate());
        author.setNationality(request.nationality());
    }

    public AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
                author.getId(), author.getFirstName(), author.getLastName(),
                author.getBirthDate(), author.getNationality(), author.getCreatedAt()
        );
    }
}