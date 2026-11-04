package com.example.book_catalog.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AuthorResponse(
    Long id, String firstName, 
    String lastName, 
    LocalDate birthDate, 
    String nationality, 
    LocalDateTime createdAt
) {}