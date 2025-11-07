package com.Management.Library.repository;

import com.Management.Library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This is a Spring Data JPA Repository.
 * THIS IS MAGIC! You don't need to write any code here.
 *
 * By extending JpaRepository<Book, Long>, Spring automatically creates
 * methods for you like:
 * - save(Book)
 * - findById(Long)
 * - findAll()
 * - delete(Book)
 * - ...and many more!
 *
 * We just add custom "finder" methods if we need them.
 */
@Repository // Tells Spring this is a Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Spring Data JPA will automatically create a query for this method
    // based on the method name.
    // "Find me a Book by its ISBN"
    Optional<Book> findByIsbn(String isbn);
}
