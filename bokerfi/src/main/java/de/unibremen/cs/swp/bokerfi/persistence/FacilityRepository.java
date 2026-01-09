package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Optional<Facility> findByUuid(Long uuid);
}
