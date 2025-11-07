package com.Management.Library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the evolved Patron class.
 * It is now a JPA @Entity.
 */
@Entity
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String patronId; // A business ID, separate from the database ID.

    // --- Relationship ---
    // A Patron has "One" relationship to "Many" Books.
    // "mappedBy = 'borrowedBy'" tells JPA that the 'borrowedBy' field in the
    // Book class is the "owner" of this relationship.
    // This prevents a 'patron_book' join table from being created.
    @OneToMany(
            mappedBy = "borrowedBy",
            cascade = CascadeType.ALL, // e.g., if you delete a Patron, delete their books? Be careful!
            orphanRemoval = true
    )
    @JsonManagedReference // Manages the "forward" part of the relationship for JSON.
    private List<Book> borrowedBooks = new ArrayList<>();

    // --- Constructors ---
    public Patron() {}

    public Patron(String name, String patronId) {
        this.name = name;
        this.patronId = patronId;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    // --- Helper Methods ---
    // These methods safely manage the bidirectional relationship.
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
        book.setBorrowedBy(this);
        book.setAvailable(false);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
        book.setBorrowedBy(null);
        book.setAvailable(true);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", patronId='" + patronId + '\'' +
                ", booksBorrowed=" + borrowedBooks.size() +
                '}';
    }
}
