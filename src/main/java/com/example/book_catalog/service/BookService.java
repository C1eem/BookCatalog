package com.example.book_catalog.service;

import com.example.book_catalog.dto.BookRequest;
import com.example.book_catalog.dto.BookResponse;
import com.example.book_catalog.entity.Author;
import com.example.book_catalog.entity.Book;
import com.example.book_catalog.exception.EntityNotFoundException;
import com.example.book_catalog.mapper.BookMapper;
import com.example.book_catalog.repository.AuthorRepository;
import com.example.book_catalog.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.book_catalog.dto.PageResponse;
import com.example.book_catalog.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.Set;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        // Ищем автора. Если нет - 404
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new EntityNotFoundException("Автор с id " + request.authorId() + " не найден"));
        
        Book book = bookMapper.toEntity(request, author);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream().map(bookMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id " + id + " не найдена"));
        return bookMapper.toResponse(book);
    }

    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id " + id + " не найдена"));
        
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new EntityNotFoundException("Автор с id " + request.authorId() + " не найден"));

        bookMapper.updateEntity(book, request, author);
        return bookMapper.toResponse(book);
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Книга с id " + id + " не найдена");
        }
        bookRepository.deleteById(id);
    }

    // Белый список полей, по которым разрешена сортировка (ТЗ раздел 5.2)
    private static final Set<String> ALLOWED_SORTS = Set.of("title", "publicationYear", "price");

    @Transactional(readOnly = true)
    public PageResponse<BookResponse> findAll(Long authorId, String genre, Integer yearFrom, Integer yearTo, Pageable pageable) {
        // 1. Проверяем размер страницы (max 50)
        if (pageable.getPageSize() > 50) {
            throw new BadRequestException("Размер страницы (size) не может превышать 50");
        }

        // 2. Проверяем поля сортировки
        for (Sort.Order order : pageable.getSort()) {
            if (!ALLOWED_SORTS.contains(order.getProperty())) {
                throw new BadRequestException("Сортировка по полю '" + order.getProperty() + "' не разрешена. Доступно: " + ALLOWED_SORTS);
            }
        }

        // 3. Запрашиваем данные с фильтрами
        Page<Book> page = bookRepository.findWithFilters(authorId, genre, yearFrom, yearTo, pageable);

        // 4. Превращаем в наш PageResponse
        return new PageResponse<>(
                page.getContent().stream().map(bookMapper::toResponse).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}