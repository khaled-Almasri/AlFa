package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.AssetType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository für den Zugriff auf Asset-Typen.
 */
@Repository
public interface AssetTypeRepository extends BaseRepository<AssetType> {
    Optional<AssetType> findByName(String name);
    boolean existsByName(String name);
}
