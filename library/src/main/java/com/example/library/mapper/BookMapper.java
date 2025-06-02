package com.example.library.mapper;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;

public class BookMapper {

    public static BookDTO toBookDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.isIssued(),
                book.getUser() != null ? book.getUser().getId() : null // Prevent null user reference
        );
    }
}