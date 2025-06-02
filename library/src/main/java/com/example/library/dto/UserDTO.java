package com.example.library.dto;

import java.util.List;

public class UserDTO {
    private int id;
    private String name;
    private int maxBooksAllowed;
    private String userType;
    private List<BookDTO> bookList;

    public UserDTO() {
    }

    public UserDTO(int id, String name, int maxBooksAllowed, String userType, List<BookDTO> bookList) {
        this.id = id;
        this.name = name;
        this.maxBooksAllowed = maxBooksAllowed;
        this.userType = userType;
        this.bookList = bookList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public void setMaxBooksAllowed(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<BookDTO> getBookList() {
        return bookList;
    }

    public void setBookList(List<BookDTO> bookList) {
        this.bookList = bookList;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxBooksAllowed=" + maxBooksAllowed +
                ", userType='" + userType + '\'' +
                ", bookList=" + bookList +
                '}';
    }
}