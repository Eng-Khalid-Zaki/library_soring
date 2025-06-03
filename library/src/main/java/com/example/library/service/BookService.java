package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private BookRepository bookRepository;
    private UserRepository userRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookMapper::toBookDTO).collect(Collectors.toList());
    }

    @Transactional
    public BookDTO getBook(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(BookMapper::toBookDTO).orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    @Transactional
    public List<BookDTO> getBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        return books.stream()
                .map(BookMapper::toBookDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<BookDTO> getBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream()
                .map(BookMapper::toBookDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<BookDTO> filterBooksByAvailability(boolean isIssued) {
        List<Book> fetchedBooks =  bookRepository.findByIsIssued(isIssued);
        return fetchedBooks.stream()
                .map(BookMapper::toBookDTO).collect(Collectors.toList());
    }

    @Transactional
    public Book addBook(BookDTO newBookDTO) {
        Book newBook = new Book(newBookDTO.getTitle(), newBookDTO.getAuthor(), newBookDTO.isIssued());
        return bookRepository.save(newBook);
    }

    @Transactional
    public Book updateBook(int id, BookDTO bookDTO) {
        Book currentBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("No Book found with this ID: " + id));

        currentBook.setTitle(bookDTO.getTitle());
        currentBook.setAuthor(bookDTO.getAuthor());
        currentBook.setIssued(bookDTO.isIssued());

        if(bookDTO.isIssued()) {
            if (bookDTO.getUserId() != null) {
                User user = userRepository.findById(bookDTO.getUserId())
                        .orElseThrow(() -> new UserNotFoundException("No User found with this ID: " + bookDTO.getUserId()));
                currentBook.setUser(user);
            } else {
                currentBook.setUser(null);
            }
        }else {
            currentBook.setUser(null);
        }
        return bookRepository.saveAndFlush(currentBook);
    }

    @Transactional
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }
}
