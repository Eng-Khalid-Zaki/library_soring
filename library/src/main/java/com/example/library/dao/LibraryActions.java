package com.example.library.dao;

import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.exception.BookNotAvailableException;
import com.example.library.exception.ExceedUserLimitException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class LibraryActions {
    private EntityManager entityManager;

    @Autowired
    public LibraryActions(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void createUser(User user) {
        entityManager.persist(user);
    } //todo finished

    @Transactional
    public void updateUserName(String name, int userId) throws NoSuchElementException {
        User currentUser = entityManager.find(User.class, userId);
        if (currentUser == null) {
            throw new NoSuchElementException("there is no user with this id");
        }
        currentUser.setName(name);
    } // todo finished

    @Transactional
    public void updateUserMaxBooksAllowed(int maxBookAllowed, int userId) throws NoSuchElementException {
        User currentUser = entityManager.find(User.class, userId);
        if (currentUser == null) {
            throw new NoSuchElementException("there is no user with this id");
        }
        currentUser.setMaxBooksAllowed(maxBookAllowed);
    } // todo finished

    @Transactional
    public void updateUserType(String newType, int userId) throws NoSuchElementException {
        User currentUser = entityManager.find(User.class, userId);
        if (currentUser == null) {
            throw new NoSuchElementException("there is no user with this id");
        }

        if (!newType.equals("student") && !newType.equals("teacher")) {
            throw new IllegalArgumentException("Only supported student and teacher types");
        }

        currentUser.setUserType(newType);
    } //todo

    @Transactional
    public void deleteUser(int id) throws NoSuchElementException {
        User user = entityManager.find(User.class, id);

        if (user == null) {
            throw new NoSuchElementException("There is no user with this id: " + id);
        }

        for (Book b : getUserBooks(id)) {
            b.setUser(null);
        }

        entityManager.remove(user);
    } // todo finished

    @Transactional
    public void createBook(Book book) {
        entityManager.persist(book);
    } // todo finished

    @Transactional
    public void deleteBook(int id) throws NoSuchElementException {
        Book book = entityManager.find(Book.class, id);
        if (book == null) {
            throw new NoSuchElementException("There is no book with this id: " + id);
        }

        if (book.isIssued()) {
            User user = book.getUser();
            user.getBookList().removeIf(b -> b.getId() == id);
            book.setUser(null);
        }

        entityManager.remove(book);
    } // todo finished

    @Transactional
    public List<Book> getUserBooks(int userId) throws NoSuchElementException {
        User currentUser = entityManager.find(User.class, userId);

        if (currentUser == null) {
            throw new NoSuchElementException("There is no user with this id: " + userId);
        }

        TypedQuery<Book> query = entityManager.createQuery(
                "FROM Book b WHERE b.user.id = :userId", Book.class
        );
        query.setParameter("userId", userId);

        return query.getResultList();
    } // todo finished

    @Transactional
    public void issueBook(int bookId, int userId) throws NoSuchElementException, BookNotAvailableException, ExceedUserLimitException {
        Book book = entityManager.find(Book.class, bookId);
        if (book == null) {
            throw new NoSuchElementException("There is no book with this id: " + bookId);
        }

        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new NoSuchElementException("There is no user with this id: " + userId);
        }

        if (user.getBookList().size() >= user.getMaxBooksAllowed()) {
            throw new ExceedUserLimitException("You have reached your limit, Please return a book before issuing another one!");
        }

        if (book.isIssued()) {
            throw new BookNotAvailableException("This book is already issued!");
        }

        book.setUser(user);
        book.setIssued(true);
        List<Book> newBookList = user.getBookList();
        newBookList.add(book);
        user.setBookList(newBookList);
    } // todo finished

    @Transactional
    public void returnBook(int userId, int bookId) throws NoSuchElementException, BookNotAvailableException {
        Book book = entityManager.find(Book.class, bookId);
        if (book == null) {
            throw new NoSuchElementException("There is no book with this id:" + bookId);
        }

        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new NoSuchElementException("There is no user with this id:" + userId);
        }

        if (!book.isIssued()) {
            throw new BookNotAvailableException("This book is not issued to be returned");
        }

        if (book.getUser().getId() != userId) {
            throw new BookNotAvailableException("This book is issued by another one");
        }

        book.setUser(null);
        user.getBookList().removeIf(b -> b.getId() == bookId);
    } // todo finished


}
