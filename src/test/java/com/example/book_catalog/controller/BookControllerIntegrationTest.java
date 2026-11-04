package com.example.book_catalog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Поднимает весь контекст Spring
@AutoConfigureMockMvc // Настраивает инструмент для имитации HTTP-запросов
@Transactional // Откатывает изменения в БД после каждого теста (чтобы тесты не влияли друг на друга)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createBook_ShouldReturn400_WhenTitleIsEmpty() throws Exception {
        String invalidJson = """
                {
                    "title": "",
                    "authorId": 1
                }
                """;

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest()) // Ожидаем 400
                .andExpect(jsonPath("$.message").value("Ошибка валидации")) // Наш формат ошибки
                .andExpect(jsonPath("$.fieldErrors[0].field").value("title"));
    }

    @Test
    void getAllBooks_ShouldReturn200_WithPaginationStructure() throws Exception {
        mockMvc.perform(get("/api/v1/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk()) // Ожидаем 200
                .andExpect(jsonPath("$.content").isArray()) // Есть массив content
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void getBookById_ShouldReturn404_WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/books/9999"))
                .andExpect(status().isNotFound()) // Ожидаем 404
                .andExpect(jsonPath("$.message").value("Книга с id 9999 не найдена"));
    }
}