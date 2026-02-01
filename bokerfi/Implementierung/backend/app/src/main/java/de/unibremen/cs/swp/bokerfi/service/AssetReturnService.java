package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository;
import de.unibremen.cs.swp.bokerfi.model.Person;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * Service für die Rückgabe von Assets.
 * <p>
 * Handhabt den Rückgabeprozess, aktualisiert den Asset-Status und schließt die Zuweisungshistorie ab.
 * </p>
 */
@Service
@org.springframework.transaction.annotation.Transactional
public class AssetReturnService {

    private final AssetRepository assetRepository;
    private final AssignmentRepository assignmentRepository;
    private final HistoryService historyService;
    
    private static final String ASSET_NOT_FOUND = "Asset not found";

    /**
     * Erstellt einen neuen AssetReturnService.
     *
     * @param assetRepository      Das Asset-Repository
     * @param assignmentRepository Das Zuweisungs-Repository
     */
    public AssetReturnService(AssetRepository assetRepository, AssignmentRepository assignmentRepository, HistoryService historyService) {
        this.assetRepository = assetRepository;
        this.assignmentRepository = assignmentRepository;
        this.historyService = historyService;
    }

    /**
     * Gibt ein Asset zurück.
     *
     * @param assetUuid Die UUID des zurückzugebenden Assets
     */
    /**
     * Markiert ein Asset für die endgültige Rückgabe (durch Mitarbeiter).
     *
     * @param assetUuid Die UUID des Assets
     */
    public void markForFinalReturn(long assetUuid) {
        Asset asset = assetRepository.findByUuid(assetUuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, ASSET_NOT_FOUND));

        // If already waiting, do nothing (Idempotent)
        if (asset.getStatus() == AssetStatus.WAITING_FOR_FINAL_RETURN) {
            return;
        }

        if (asset.getStatus() != AssetStatus.ASSIGNED) {
             throw new BokerfiException(ErrorCode.CONFLICT, "Asset is not assigned");
        }
        
        asset.setStatus(AssetStatus.WAITING_FOR_FINAL_RETURN);
        assetRepository.save(asset);
        
        historyService.logEvent(asset, "MARKED_FOR_FINAL_RETURN", "Asset marked for final return", (Person) null);
    }

    /**
     * Bestätigt die endgültige Rückgabe (durch Manager).
     *
     * @param assetUuid Die UUID des Assets
     */
    public void confirmFinalReturn(long assetUuid) {
        Asset asset = assetRepository.findByUuid(assetUuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, ASSET_NOT_FOUND));

        if (asset.getStatus() != AssetStatus.WAITING_FOR_FINAL_RETURN) {
             throw new BokerfiException(ErrorCode.CONFLICT, "Asset is not waiting for final return. Current status: " + asset.getStatus());
        }

        asset.setStatus(AssetStatus.RETURNED_FINAL);
        
        // Close assignment
        assignmentRepository.findByAssetAndReturnedAtIsNull(asset).ifPresent(assignment -> {
             assignment.setReturnedAt(LocalDateTime.now());
             assignmentRepository.save(assignment);
        });
        
        asset.setAssignedPerson(null);

        assetRepository.save(asset);
        
        historyService.logEvent(asset, "RETURNED_FINAL", "Asset return confirmed", (Person) null);
    }

    /**
     * Legacy/Shutdown method (Direct Return).
     */
    public void returnAsset(long assetUuid) {
       Asset asset = assetRepository.findByUuid(assetUuid)
               .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, ASSET_NOT_FOUND));
       
       switch (asset.getStatus()) {
           case ASSIGNED -> {
               // Direct return (Manager shortcut)
               asset.setStatus(AssetStatus.RETURNED_FINAL);
               assignmentRepository.findByAssetAndReturnedAtIsNull(asset).ifPresent(a -> {
                   a.setReturnedAt(LocalDateTime.now());
                   assignmentRepository.save(a);
               });
               asset.setAssignedPerson(null);
               assetRepository.save(asset);
               historyService.logEvent(asset, "RETURNED_FINAL", "Asset returned directly", (Person) null);
           }
           case WAITING_FOR_FINAL_RETURN -> confirmFinalReturn(assetUuid);
           default -> throw new BokerfiException(ErrorCode.CONFLICT, "Cannot return asset from status: " + asset.getStatus());
       }
    }

    /**
     * Markiert ein Asset für die Rückgabe zur Reparatur (durch Mitarbeiter).
     *
     * @param assetUuid Die UUID des Assets
     */
    public void markForRepairReturn(long assetUuid) {
        Asset asset = assetRepository.findByUuid(assetUuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, ASSET_NOT_FOUND));

        if (asset.getStatus() != AssetStatus.ASSIGNED) {
             throw new BokerfiException(ErrorCode.CONFLICT, "Asset is not assigned");
        }
        
        asset.setStatus(AssetStatus.WAITING_FOR_REPAIR_RETURN);
        assetRepository.save(asset);
        
        historyService.logEvent(asset, "MARKED_FOR_REPAIR_RETURN", "Asset marked for repair return", (Person) null);
    }

    /**
     * Bestätigt die Rückgabe zur Reparatur.
     *
     * @param assetUuid Die UUID des Assets
     */
    public void confirmRepairReturn(long assetUuid) {
        Asset asset = assetRepository.findByUuid(assetUuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, ASSET_NOT_FOUND));

        if (asset.getStatus() != AssetStatus.WAITING_FOR_REPAIR_RETURN) {
             throw new BokerfiException(ErrorCode.CONFLICT, "Asset is not waiting for repair return");
        }

        asset.setStatus(AssetStatus.RETURNED_REPAIR);
        
        // For Repair Return, we keep the assignment active so we know who to give it back to.
        // We do NOT close the assignment here.
        // asset.setAssignedPerson(null); // Keep assigned person

        assetRepository.save(asset);
        
        historyService.logEvent(asset, "RETURNED_FOR_REPAIR", "Asset returned for repair confirmed", (Person) null);
    }
}
