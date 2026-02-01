package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service für die Aussonderung (Retirement) von Assets.
 */
@Service
@Transactional
public class AssetRetireService {

    private final AssetRepository assetRepository;
    private final HistoryService historyService;

    public AssetRetireService(AssetRepository assetRepository, HistoryService historyService) {
        this.assetRepository = assetRepository;
        this.historyService = historyService;
    }

    /**
     * Sondert ein Asset aus (Status auf RETIRED setzen).
     *
     * @param uuid Die UUID des Assets.
     */
    public void retire(long uuid) {
        Asset asset = assetRepository.findByUuid(uuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Asset not found"));

        // Only allow retirement from certain states?
        // Typically: IN_STOCK, MAINTENANCE_FINAL, RETURNED_FINAL
        // BUT: User screenshot status is IN_REPAIR (MAINTENANCE_REPAIR).
        // Let's allow retirement from any unassigned state OR maintenance state.
        // It shouldn't be assigned to someone actively (unless we forcefully unassign, but let's be safe).

        if (asset.getStatus() == AssetStatus.ASSIGNED ||
            asset.getStatus() == AssetStatus.WAITING_FOR_REPAIR_RETURN ||
            asset.getStatus() == AssetStatus.WAITING_FOR_FINAL_RETURN) {
            throw new BokerfiException(ErrorCode.CONFLICT, "Cannot retire asset currently assigned or waiting for return. Return it first.");
        }

        asset.setStatus(AssetStatus.RETIRED);
        asset.setAssignedPerson(null); // Should be null anyway if not assigned

        assetRepository.save(asset);
        historyService.logEvent(asset, "RETIRED", "Asset retired/scrapped", (Person) null);
    }
}
