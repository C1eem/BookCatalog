package com.example.book_catalog.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        Integer publicationYear,
        String genre,
        BigDecimal price,
        Long authorId,         
        String authorFullName, 
        LocalDateTime createdAt
) {}