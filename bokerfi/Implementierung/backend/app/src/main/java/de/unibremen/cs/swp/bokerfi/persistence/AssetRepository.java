package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.Asset;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository für den Zugriff auf Assets.
 */
@Repository
public interface AssetRepository extends BaseRepository<Asset>, JpaSpecificationExecutor<Asset> {
    Optional<Asset> findByInventoryNumber(String inventoryNumber);
    
    // For Employee View
    java.util.List<Asset> findAllByAssignedPersonEmail(String email);
}
