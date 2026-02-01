package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetHistoryRepository extends JpaRepository<AssetHistory, Long> {

    List<AssetHistory> findAllByAsset(Asset asset);
    
    List<AssetHistory> findAllByAssetOrderByTimestampDesc(Asset asset);

}
