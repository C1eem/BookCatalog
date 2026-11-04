package com.example.book_catalog.repository;

import com.example.book_catalog.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE " +
           "(:authorId IS NULL OR b.author.id = :authorId) AND " +
           "(:genre IS NULL OR b.genre = :genre) AND " +
           "(:yearFrom IS NULL OR b.publicationYear >= :yearFrom) AND " +
           "(:yearTo IS NULL OR b.publicationYear <= :yearTo)")
    Page<Book> findWithFilters(
            @Param("authorId") Long authorId,
            @Param("genre") String genre,
            @Param("yearFrom") Integer yearFrom,
            @Param("yearTo") Integer yearTo,
            Pageable pageable
    );

    boolean existsByAuthorId(Long authorId);
    
    List<Book> findByAuthorId(Long authorId);
}