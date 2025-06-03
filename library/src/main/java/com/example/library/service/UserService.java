package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.exception.BookNotAvailableException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.ExceedUserLimitException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import com.example.library.dto.UserDTO;
import com.example.library.entity.Book;
import com.example.library.entity.Student;
import com.example.library.entity.Teacher;
import com.example.library.entity.User;
import com.example.library.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
    private BookRepository bookRepository;

    @Autowired
    public UserService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(int id) {
        User currentUser = userRepository.getReferenceById(id);
        return UserMapper.toUserDTO(currentUser);
    }

    public List<UserDTO> getUsersByName(String name) {
        List<User> users = userRepository.findByName(name);
        return users.stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    public List<UserDTO> findUsersByType(String userType) {
        List<User> users = userRepository.findAll();

        List<User> filteredUsers = users.stream()
                .filter(user -> (userType.equalsIgnoreCase("student") && user instanceof Student) ||
                        (userType.equalsIgnoreCase("teacher") && user instanceof Teacher))
                .collect(Collectors.toList());

        return filteredUsers.stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public User addUser(UserDTO userDTO) {
        User newUser = switch (userDTO.getUserType().toLowerCase()) {
            case "student" -> new Student(userDTO.getName(), userDTO.getMaxBooksAllowed(), "student");
            case "teacher" -> new Teacher(userDTO.getName(), userDTO.getMaxBooksAllowed(), "teacher");
            default -> throw new IllegalArgumentException("Invalid userType: " + userDTO.getUserType());
        };

        List<Book> books = Optional.ofNullable(userDTO.getBookList())
                .orElse(List.of())
                .stream()
                .map(bookDTO -> {
                    Book book = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.isIssued());
                    book.setUser(newUser);
                    return book;
                })
                .collect(Collectors.toList());

        newUser.setBookList(books);

        return userRepository.saveAndFlush(newUser);
    }

    public User updateUser(int id, UserDTO currentUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        existingUser.setName(currentUser.getName());
        existingUser.setMaxBooksAllowed(currentUser.getMaxBooksAllowed());

        List<Book> books = currentUser.getBookList() != null
                ? currentUser.getBookList().stream()
                .map(bookDTO -> {
                    Book book = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.isIssued());
                    book.setUser(existingUser);
                    return book;
                })
                .collect(Collectors.toList())
                : List.of();

        existingUser.setBookList(books);

        return userRepository.saveAndFlush(existingUser);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public List<BookDTO> findUserBooks(int id) {
        boolean userExists = userRepository.existsById(id);
        if (!userExists) {
            throw new UserNotFoundException("No user found with ID: " + id);
        }

        List<Book> userBooks = bookRepository.findByUserId(id);
        return userBooks.stream()
                .map(BookMapper::toBookDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void issueBook(int userId, int bookId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

        if (existingUser.getBookList().size() >= existingUser.getMaxBooksAllowed()) {
            throw new ExceedUserLimitException("You have reached your limit, Please return a book before issuing a new one");
        }

        if (existingBook.isIssued()) {
            throw new BookNotAvailableException("This book is already issued by a user!");
        }

        existingBook.setUser(existingUser);
        existingBook.setIssued(true);

        existingUser.getBookList().add(existingBook);

        bookRepository.save(existingBook);
        userRepository.save(existingUser);
    }

    @Transactional
    public void returnBook(int userId, int bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with ID: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("No book found with ID: " + bookId));

        if (!book.isIssued()) {
            throw new BookNotAvailableException("This book is not issued to be returned");
        }

        if (!user.getBookList().contains(book)) {
            throw new BookNotAvailableException("You did not issue this book to return it");
        }

        List<Book> newBookList = user.getBookList().stream()
                .filter(b -> b.getId() != book.getId())
                .collect(Collectors.toList());

        book.setIssued(false);
        book.setUser(null);

        bookRepository.save(book);
        user.setBookList(newBookList);
        userRepository.save(user);
    }

}
