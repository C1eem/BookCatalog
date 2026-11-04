package com.example.book_catalog.service;

import com.example.book_catalog.dto.BookRequest;
import com.example.book_catalog.exception.EntityNotFoundException;
import com.example.book_catalog.mapper.BookMapper;
import com.example.book_catalog.repository.AuthorRepository;
import com.example.book_catalog.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void create_ShouldThrowNotFound_WhenAuthorDoesNotExist() {
        // Arrange
        BookRequest request = new BookRequest("Книга", "1234567890", 2000, "Жанр", BigDecimal.TEN, 999L);
        when(authorRepository.findById(999L)).thenReturn(Optional.empty()); // Автор не найден

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookService.create(request));
        
        // Проверяем, что сохранение книги не произошло
        verify(bookRepository, never()).save(any());
    }
}