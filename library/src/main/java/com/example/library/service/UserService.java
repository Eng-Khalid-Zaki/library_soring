package com.example.library.service;

import com.example.library.exception.UserNotFoundException;
import com.example.library.repository.UserRepository;
import com.example.library.dto.UserDTO;
import com.example.library.entity.Book;
import com.example.library.entity.Student;
import com.example.library.entity.Teacher;
import com.example.library.entity.User;
import com.example.library.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        User newUser;

        // Create an instance based on userType
        switch (userDTO.getUserType().toLowerCase()) {
            case "student":
                newUser = new Student(userDTO.getName(), userDTO.getMaxBooksAllowed(), "student");
                break;
            case "teacher":
                newUser = new Teacher(userDTO.getName(), userDTO.getMaxBooksAllowed(), "teacher");
                break;
            default:
                throw new IllegalArgumentException("Invalid userType: " + userDTO.getUserType());
        }

        // Convert BookDTO list to Book entities
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

}
