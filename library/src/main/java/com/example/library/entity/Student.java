package com.example.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("student")
public class Student extends User{

    public Student() {
    }

    public Student(String name, int maxBooksAllowed, String userType) {
        super(name, maxBooksAllowed);
    }
}
