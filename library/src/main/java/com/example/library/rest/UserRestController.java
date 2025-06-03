package com.example.library.rest;

import com.example.library.dto.BookDTO;
import com.example.library.dto.UserDTO;
import com.example.library.entity.User;
import com.example.library.repository.BookRepository;
import com.example.library.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> findAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/id/{id}")
    public UserDTO findUserById(@PathVariable int id) {
        return userService.getUser(id);
    }

    @GetMapping("/name/{name}")
    public List<UserDTO> findByName(@PathVariable String name) {
        return userService.getUsersByName(name);
    }

    @GetMapping("/students")
    public List<UserDTO> findStudents() {
        return userService.findUsersByType("student");
    }

    @GetMapping("/teachers")
    public List<UserDTO> findTeachers() {
        return userService.findUsersByType("teacher");
    }

    @PostMapping
    public User addUser(@RequestBody UserDTO newUser) {
        return userService.addUser(newUser);
    }

    @PatchMapping("id/{id}")
    public User updateUser(@PathVariable int id, @RequestBody UserDTO UpdatedUser) {
        return userService.updateUser(id, UpdatedUser);
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/id/{id}/books")
    public List<BookDTO> getUserBooks(@PathVariable int id) {
        return userService.findUserBooks(id);
    }

    @PostMapping("/issue")
    public ResponseEntity<String> issueBook(@RequestParam int userId, @RequestParam int bookId) {
        userService.issueBook(userId, bookId);
        return ResponseEntity.ok("Book issued successfully");
    }

    @PutMapping("return")
    public ResponseEntity<String> returnBook(@RequestParam int userId, @RequestParam int bookId) {
        userService.returnBook(userId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }

}
