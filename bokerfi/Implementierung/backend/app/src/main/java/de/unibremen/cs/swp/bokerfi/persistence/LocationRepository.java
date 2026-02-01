package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.Location;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository für den Zugriff auf Standorte.
 */
@Repository
public interface LocationRepository extends BaseRepository<Location> {
    Optional<Location> findByName(String name);
}
