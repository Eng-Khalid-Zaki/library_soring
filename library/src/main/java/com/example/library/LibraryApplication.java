package com.example.library;

import com.example.library.dao.LibraryActions;
import com.example.library.entity.Book;
import com.example.library.entity.Student;
import com.example.library.entity.Teacher;
import com.example.library.entity.User;
import com.example.library.exception.BookNotAvailableException;
import com.example.library.exception.ExceedUserLimitException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(LibraryActions libraryActions) {
        return runner -> {
            updateUserType(libraryActions);
        };
    }

    private void updateUserType(LibraryActions libraryActions) {
        int userId = 5;
        String type = "fmf";
        try {
            libraryActions.updateUserType(type, userId);
        }catch (NoSuchElementException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateUserMaxBooksAllowed(LibraryActions libraryActions) {
        int userId = 5;
        int maxBooksAllowed = 50;
        try {
            libraryActions.updateUserMaxBooksAllowed(maxBooksAllowed, userId);
        }catch(NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateUserName(LibraryActions libraryActions) {
        String name = "Mohsen";
        int userId = 5;
        try {
            libraryActions.updateUserName(name, userId);
        }catch(NoSuchElementException e) {
            System.out.println(e.getMessage());
        }

    }

    private void returnBook(LibraryActions libraryActions) {
        int bookId = 4;
        int userId = 3;
        try {
            libraryActions.returnBook(userId, bookId);
        }catch(NoSuchElementException | BookNotAvailableException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteBook(LibraryActions libraryActions) {
        try {
            libraryActions.deleteBook(1011);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createBook(LibraryActions libraryActions) {
        Book newBook = new Book("C++ Advanced", "Mohsen", false);
        libraryActions.createBook(newBook);
    }

    private void issueBook(LibraryActions libraryActions) {
        try {
            libraryActions.issueBook(7, 3);
        } catch (NoSuchElementException | BookNotAvailableException | ExceedUserLimitException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Finished");

    }

    private void deleteUser(LibraryActions libraryActions) {
        int id = 10;
        try {
            libraryActions.deleteUser(id);
        }catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }

    }

    private void addNewStudent(LibraryActions libraryActions) {
        User student = new Student("Khalid", 10, "student");
        libraryActions.createUser(student);
    }

    private void addNewTeacher(LibraryActions libraryActions) {
        User teacher = new Teacher("Mostafa", 20, "teacher");
        libraryActions.createUser(teacher);
    }

    private List<Book> getUserBooks(LibraryActions libraryActions) {
        int id = 3;
        try {
            return libraryActions.getUserBooks(id);
        }catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
       return null;
    }
}
