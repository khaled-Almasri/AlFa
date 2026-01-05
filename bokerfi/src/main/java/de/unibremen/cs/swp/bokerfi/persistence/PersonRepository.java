package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository für Personen.
 * <p>
 * Dieses Repository basiert auf Spring Data JPA.
 * Alle CRUD-Operationen werden automatisch bereitgestellt.
 * </p>
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // KEINE Methoden nötig!
}