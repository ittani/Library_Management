package com.Management.Library.contoller;

import com.Management.Library.model.Book;
import com.Management.Library.model.Patron;
import com.Management.Library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is the new "UI" layer. It's a @RestController.
 * It replaces the command-line menu.
 * It exposes "endpoints" (URLs) that a web browser or mobile app
 * can interact with. It returns data as JSON.
 */
@RestController
@RequestMapping("/api") // All endpoints in this class will start with /api
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // --- Book Endpoints ---

    // POST /api/books - Adds a new book
    @PostMapping("/books")
    public Book createBook(@RequestBody Book book) {
        // @RequestBody tells Spring to parse the JSON body of the request
        // into a Book object.
        return libraryService.addBook(book);
    }

    // GET /api/books - Gets all books
    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return libraryService.getAllBooks();
    }

    // GET /api/books/available - Gets available books
    @GetMapping("/books/available")
    public List<Book> getAvailableBooks() {
        return libraryService.getAvailableBooks();
    }

    // --- Patron Endpoints ---

    // POST /api/patrons - Adds a new patron
    @PostMapping("/patrons")
    public Patron createPatron(@RequestBody Patron patron) {
        return libraryService.addPatron(patron);
    }

    // GET /api/patrons - Gets all patrons
    @GetMapping("/patrons")
    public List<Patron> getAllPatrons() {
        return libraryService.getAllPatrons();
    }

    // --- Core Logic Endpoints ---

    // A simple DTO (Data Transfer Object) class to handle the checkout request
    // This is defined inside the controller for simplicity.
    static class CheckoutRequest {
        public String patronId;
        public String isbn;
    }

    // POST /api/checkout - Checks out a book to a patron
    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutBook(@RequestBody CheckoutRequest request) {
        try {
            String message = libraryService.checkOutBook(request.patronId, request.isbn);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST /api/return - Returns a book
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestBody CheckoutRequest request) {
        try {
            String message = libraryService.returnBook(request.patronId, request.isbn);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
