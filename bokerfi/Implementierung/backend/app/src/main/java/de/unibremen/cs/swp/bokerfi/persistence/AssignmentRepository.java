package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.Assignment;
import de.unibremen.cs.swp.bokerfi.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository für den Zugriff auf Zuweisungen (Assignments).
 */
@Repository
public interface AssignmentRepository extends BaseRepository<Assignment> {
    List<Assignment> findByAsset(Asset asset);
    List<Assignment> findByPerson(Person person);
    
    // Find active assignment for asset
    Optional<Assignment> findByAssetAndReturnedAtIsNull(Asset asset);

    java.util.List<Assignment> findByReturnDeadlineBeforeAndReturnedAtIsNull(java.time.LocalDateTime deadline);

    java.util.List<Assignment> findByReturnDeadlineBetweenAndReturnedAtIsNull(java.time.LocalDateTime start, java.time.LocalDateTime end);

    java.util.List<Assignment> findByPersonEmailAndReturnedAtIsNull(String email);
}
