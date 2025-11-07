package com.Management.Library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * This is the evolved Book class.
 * It is now a JPA @Entity, meaning it will be mapped to a database table.
 */
@Entity // Tells JPA that this class is an entity and should be persisted.
public class Book {

    @Id // Marks this field as the Primary Key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID.
    private Long id; // Using Long for database IDs is standard.

    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable = true;

    // --- Relationship ---
    // A Book is "borrowed by" one Patron.
    // This is the "Many" side of a One-to-Many relationship.
    @ManyToOne
    @JoinColumn(name = "patron_id") // This creates a 'patron_id' foreign key column in the 'book' table.
    @JsonBackReference // Prevents infinite loops when serializing to JSON.
    private Patron borrowedBy;

    // --- Constructors ---
    // A no-arg constructor is required by JPA
    public Book() {}

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
    }

    // --- Getters and Setters ---
    // JPA uses getters and setters to access the fields.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Patron getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(Patron borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
