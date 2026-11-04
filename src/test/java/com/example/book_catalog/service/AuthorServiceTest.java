package com.example.book_catalog.service;

import com.example.book_catalog.exception.ConflictException;
import com.example.book_catalog.exception.EntityNotFoundException;
import com.example.book_catalog.mapper.AuthorMapper;
import com.example.book_catalog.mapper.BookMapper;
import com.example.book_catalog.repository.AuthorRepository;
import com.example.book_catalog.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Включаем Mockito
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorMapper authorMapper;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks // Автоматически передает моки в конструктор AuthorService
    private AuthorService authorService;

    @Test
    void delete_ShouldThrowConflictException_WhenAuthorHasBooks() {
        // Arrange (Подготовка)
        Long authorId = 1L;
        when(authorRepository.existsById(authorId)).thenReturn(true);
        when(bookRepository.existsByAuthorId(authorId)).thenReturn(true); // Говорим моку, что книги есть

        // Act & Assert (Действие и проверка)
        assertThrows(ConflictException.class, () -> authorService.delete(authorId));
        
        // Проверяем, что метод удаления так и не был вызван
        verify(authorRepository, never()).deleteById(authorId);
    }

    @Test
    void delete_ShouldThrowEntityNotFoundException_WhenAuthorNotFound() {
        Long authorId = 999L;
        when(authorRepository.existsById(authorId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> authorService.delete(authorId));
        verify(bookRepository, never()).existsByAuthorId(authorId);
    }
}