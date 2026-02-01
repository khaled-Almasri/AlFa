package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssetHistoryRepository;
import org.springframework.stereotype.Service;

/**
 * Service zum Löschen von Assets.
 * <p>
 * Stellt sicher, dass alle verknüpften Daten (Zuweisungen) vor dem Löschen bereinigt werden.
 * </p>
 */
@Service
public class AssetDeleteService {

    private final AssetRepository repository;
    private final de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository assignmentRepository;
    private final AssetHistoryRepository assetHistoryRepository;

    /**
     * Erstellt einen neuen AssetDeleteService.
     *
     * @param repository           Das Asset-Repository
     * @param assignmentRepository Das Zuweisungs-Repository
     */
    public AssetDeleteService(AssetRepository repository, 
                              de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository assignmentRepository,
                              AssetHistoryRepository assetHistoryRepository) {
        this.repository = repository;
        this.assignmentRepository = assignmentRepository;
        this.assetHistoryRepository = assetHistoryRepository;
    }

    /**
     * Löscht ein Asset anhand seiner UUID.
     *
     * @param uuid Die UUID des zu löschenden Assets
     */
    @SuppressWarnings("null")
    public void delete(long uuid) {
        Asset asset = repository.findByUuid(uuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Asset not found"));
        
        // Delete all assignments (history) first to satisfy FK constraints
        assignmentRepository.deleteAll(assignmentRepository.findByAsset(asset));
        // Delete all asset history entries
        assetHistoryRepository.deleteAll(assetHistoryRepository.findAllByAsset(asset));
        
        repository.delete(asset);
    }
}
