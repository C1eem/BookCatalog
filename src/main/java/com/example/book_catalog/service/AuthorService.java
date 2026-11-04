package com.example.book_catalog.service;

import com.example.book_catalog.dto.AuthorRequest;
import com.example.book_catalog.dto.AuthorResponse;
import com.example.book_catalog.entity.Author;
import com.example.book_catalog.exception.ConflictException;
import com.example.book_catalog.exception.EntityNotFoundException;
import com.example.book_catalog.mapper.AuthorMapper;
import com.example.book_catalog.repository.AuthorRepository;
import com.example.book_catalog.repository.BookRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public AuthorResponse create(AuthorRequest request) {
        Author author = authorMapper.toEntity(request);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> findAll() {
        return authorRepository.findAll().stream().map(authorMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AuthorResponse findById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автор с id " + id + " не найден")); 
        return authorMapper.toResponse(author);
    }

    @Transactional
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автор с id " + id + " не найден")); 
        authorMapper.updateEntity(author, request);
        return authorMapper.toResponse(author); 
    }

    @Transactional
    public void delete(Long id) {
        // 1. Проверяем, существует ли автор
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Автор с id " + id + " не найден");
        }
        
        // 2. Проверяем, есть ли у него книги
        if (bookRepository.existsByAuthorId(id)) { // <--- БЕЗ восклицательного знака!
            throw new ConflictException("Нельзя удалить автора, у которого есть книги");
        }
        
        // 3. Удаляем
        authorRepository.deleteById(id);
    }
}