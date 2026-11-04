package com.example.book_catalog.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BookRequest(
        @NotBlank(message = "Название не может быть пустым")
        @Size(max = 200, message = "Название не должно превышать 200 символов")
        String title,

        @Pattern(regexp = "^[0-9-]{10,17}$", message = "ISBN должен состоять из 10 или 13 цифр (допускаются дефисы)")
        String isbn,

        @Min(value = 1000, message = "Год издания не может быть меньше 1000")
        @Max(value = 2026, message = "Год издания не может быть в будущем")
        Integer publicationYear,

        @Size(max = 50, message = "Жанр не должен превышать 50 символов")
        String genre,

        @PositiveOrZero(message = "Цена не может быть отрицательной")
        BigDecimal price,

        @NotNull(message = "ID автора обязателен")
        Long authorId
) {}