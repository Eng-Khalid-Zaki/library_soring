package com.example.library.repository;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByIsIssued(Boolean isIssued);
    List<Book> findByUserId(int userId);
}
