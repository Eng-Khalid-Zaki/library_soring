package com.example.library.dto;

import java.util.List;

public class StudentDTO extends UserDTO {

    public StudentDTO() {
    }

    public StudentDTO(int id, String name, int maxBooksAllowed, String userType, List<BookDTO> bookList) {
        super(id, name, maxBooksAllowed, userType, bookList);
    }
}