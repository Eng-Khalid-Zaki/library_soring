package com.example.library.dto;

import java.util.List;

public class TeacherDTO extends UserDTO {

    public TeacherDTO() {
    }

    public TeacherDTO(int id, String name, int maxBooksAllowed, String userType, List<BookDTO> bookList) {
        super(id, name, maxBooksAllowed, userType, bookList);
    }
}