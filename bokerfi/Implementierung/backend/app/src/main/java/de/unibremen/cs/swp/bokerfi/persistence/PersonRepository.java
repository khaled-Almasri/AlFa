package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.Person;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository für den Zugriff auf Personen.
 */
@Repository
public interface PersonRepository extends BaseRepository<Person> {
    Optional<Person> findByEmail(String email);
    Optional<Person> findByEmailIgnoreCase(String email);
    Optional<Person> findByPersonnelNumber(String personnelNumber);
    Optional<Person> findByPersonnelNumberIgnoreCase(String personnelNumber);
    long countByRoleAndActiveTrue(de.unibremen.cs.swp.bokerfi.model.Role role);
}
