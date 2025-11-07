package com.Management.Library.repository;

import com.Management.Library.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository for Patron entities.
 * Again, Spring Data JPA provides all the standard CRUD methods.
 */
@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {

    // "Find me a Patron by their patronId"
    Optional<Patron> findByPatronId(String patronId);
}
