package com.example.library.rest;


import com.example.library.dto.BookDTO;
import com.example.library.dto.UserDTO;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDTO> findAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/id/{id}")
    public BookDTO findBookById(@PathVariable int id) {
        return bookService.getBook(id);
    }

    @GetMapping("/title/{title}")
    public List<BookDTO> findByName(@PathVariable String title) {
        return bookService.getBooksByTitle(title);
    }

    @GetMapping("/author/{author}")
    public List<BookDTO> findByAuthor(@PathVariable String author) {
        return bookService.getBooksByAuthor(author);
    }

    @GetMapping("/available")
    public List<BookDTO> filterBooksByAvailability(@RequestParam boolean isIssued) {
        return bookService.filterBooksByAvailability(isIssued);
    }

    @PostMapping
    public Book addBook(@RequestBody BookDTO newBook) {
        return bookService.addBook(newBook);
    }

    @PatchMapping("id/{id}")
    public Book updateBook(@PathVariable int id, @RequestBody BookDTO UpdatedBook) {
        return bookService.updateBook(id, UpdatedBook);
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

}
