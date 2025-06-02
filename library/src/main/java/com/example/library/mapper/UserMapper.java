package com.example.library.mapper;

import com.example.library.dto.UserDTO;
import com.example.library.dto.BookDTO;
import com.example.library.entity.User;
import com.example.library.entity.Book;
import com.example.library.entity.Student;
import com.example.library.entity.Teacher;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        List<BookDTO> bookDTOList = user.getBookList() != null
                ? user.getBookList().stream()
                .map(BookMapper::toBookDTO) // Convert book list to BookDTO list
                .collect(Collectors.toList())
                : List.of(); // Avoid null lists

        // Identify the subclass for correct DTO conversion
        String userType;
        if (user instanceof Student) {
            userType = "student";
        } else if (user instanceof Teacher) {
            userType = "teacher";
        } else {
            userType = "unknown"; // Safety fallback
        }

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getMaxBooksAllowed(),
                userType, // Determined dynamically
                bookDTOList
        );
    }

    public static List<UserDTO> toUserDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }
}