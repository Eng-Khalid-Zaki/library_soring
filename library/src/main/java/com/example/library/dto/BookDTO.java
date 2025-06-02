package com.example.library.dto;

public class BookDTO {
    private int id;
    private String title;
    private String author;
    private boolean isIssued;
    private Integer userId; // Stores the user's ID instead of the full entity

    public BookDTO() {
    }

    public BookDTO(int id, String title, String author, boolean isIssued, Integer userId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = isIssued;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public void setIssued(boolean issued) {
        isIssued = issued;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isIssued=" + isIssued +
                ", userId=" + userId +
                '}';
    }
}