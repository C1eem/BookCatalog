package com.example.book_catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AuthorRequest(

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 100, message = "Имя не должно превышать 100 символов")
    String firstName,
    
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(max = 100, message = "Фамилия не должна превышать 100 символов")
    String lastName,
    
    @Past(message = "Дата рождения должна быть в прошлом")
    LocalDate birthDate,
    
    @Size(max = 100, message = "Гражданство не должно превышать 100 символов")
    String nationality
) {}