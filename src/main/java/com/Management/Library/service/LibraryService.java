package com.Management.Library.service;

import com.Management.Library.model.Book;
import com.Management.Library.model.Patron;
import com.Management.Library.repository.BookRepository;
import com.Management.Library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This is the evolved Library class, now a @Service.
 * This is where the "business logic" lives.
 * It no longer holds ArrayLists. It uses the repositories to
 * fetch and save data from/to the database.
 */
@Service
public class LibraryService {

    // --- Dependency Injection ---
    // Spring will automatically "inject" an instance of these repositories
    // when the LibraryService is created. This is @Autowired.
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    @Autowired
    public LibraryService(BookRepository bookRepository, PatronRepository patronRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    // --- Book Methods ---
    public Book addBook(Book book) {
        // The save() method handles both create and update.
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findAll().stream()
                .filter(Book::isAvailable)
                .toList();
    }

    // --- Patron Methods ---
    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    // --- Core Logic ---
    // @Transactional ensures that this method runs in a single database
    // transaction. If any part fails, the entire operation is rolled back.
    // This is CRITICAL for data integrity.
    @Transactional
    public String checkOutBook(String patronId, String isbn) {
        // We use the 'Optional' finders we defined in the repositories.
        Patron patron = patronRepository.findByPatronId(patronId)
                .orElseThrow(() -> new RuntimeException("Patron not found with ID: " + patronId));

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));

        if (!book.isAvailable()) {
            return "Error: Book is already checked out.";
        }

        // --- Success ---
        // We update the objects in memory.
        patron.borrowBook(book);

        // When the @Transactional method finishes, JPA "flushes" these
        // changes to the database automatically!
        // We don't even need to call save() explicitly, though it's good practice.
        patronRepository.save(patron); // This saves the relationship
        bookRepository.save(book);     // This saves the book's new state

        return "Success: " + patron.getName() + " checked out \"" + book.getTitle() + "\".";
    }

    @Transactional
    public String returnBook(String patronId, String isbn) {
        Patron patron = patronRepository.findByPatronId(patronId)
                .orElseThrow(() -> new RuntimeException("Patron not found with ID: " + patronId));

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));

        // Check if the patron *actually* has this book
        if (!patron.getBorrowedBooks().contains(book)) {
            return "Error: Patron does not have this book checked out.";
        }

        // --- Success ---
        // We update the objects in memory using our helper method.
        patron.returnBook(book);

        // And JPA handles the database update at the end of the transaction.
        patronRepository.save(patron);
        bookRepository.save(book);

        return "Success: " + patron.getName() + " returned \"" + book.getTitle() + "\".";
    }
}
