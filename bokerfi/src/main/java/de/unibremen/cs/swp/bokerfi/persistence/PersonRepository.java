package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUuid(Long uuid);

    void deleteByUuid(Long uuid);
}
