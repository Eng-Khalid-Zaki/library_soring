package com.example.library.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("teacher")
public class Teacher extends User {

    public Teacher() {
    }

    public Teacher(String name, int maxBooksAllowed, String userType) {
        super(name, maxBooksAllowed, userType);
    }
}
