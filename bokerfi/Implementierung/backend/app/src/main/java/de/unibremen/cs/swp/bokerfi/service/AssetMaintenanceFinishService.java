package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.infrastructure.EmailService;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service zum Beenden der Wartung von Assets.
 * <p>
 * Handhabt den Übergang von Assets aus dem Wartungsmodus, einschließlich Reparaturabschluss und Aussonderung.
 * </p>
 */
@Service
@org.springframework.transaction.annotation.Transactional
public class AssetMaintenanceFinishService {

    private final AssetRepository assetRepository;
    private final EmailService emailService;
    private final HistoryService historyService;
    private final String adminMail;
    
    private static final String ASSET_NOT_FOUND = "Asset not found";
    
    /**
     * Erstellt einen neuen AssetMaintenanceFinishService.
     *
     * @param assetRepository Das Asset-Repository
     * @param emailService    Der E-Mail-Service
     * @param adminMail       Die Admin-E-Mail-Adresse
     */
    public AssetMaintenanceFinishService(AssetRepository assetRepository,
                                         EmailService emailService,
                                         HistoryService historyService,
                                         @Value("${bokerfi.mail.admin}") String adminMail) {
        this.assetRepository = assetRepository;
        this.emailService = emailService;
        this.historyService = historyService;
        this.adminMail = adminMail;
    }

    /**
     * Beendet die Wartung (generisch).
     *
     * @param assetUuid Die UUID des Assets
     */
    public void finishMaintenance(long assetUuid) {

        Asset asset = assetRepository.findByUuid(assetUuid)
                .orElseThrow(() -> new BokerfiException(
                        ErrorCode.ENTITY_DOES_NOT_EXIST,
                        ASSET_NOT_FOUND
                ));
        
        // MAINTENANCE_REPAIR -> READY_FOR_REPAIR_PICKUP (Manager: FINISH_REPAIR)
        // MAINTENANCE_FINAL -> RETIRED (Manager: RETIRE)
        // Diagram: MAINTENANCE_REPAIR -(Manager: FINISH_REPAIR)-> READY_FOR_REPAIR_PICKUP
        // MAINTENANCE_FINAL -(Manager: RETIRE)-> RETIRED.
        
        // Assuming "Finish" means repair done.
        // Generic finish - keeps legacy behavior if needed, or strictly repair?
        // Let's assume generic finish handles both if called via generic endpoint.
        
        switch (asset.getStatus()) {
            case MAINTENANCE_REPAIR -> asset.setStatus(AssetStatus.READY_FOR_REPAIR_PICKUP);
            case MAINTENANCE_FINAL -> asset.setStatus(AssetStatus.RETIRED);
            case RETIRED -> {
                return; // Idempotent
            }
            default -> throw new BokerfiException(ErrorCode.CONFLICT, "Asset is not in maintenance or returned state (Status: " + asset.getStatus() + ")");
        }
        
        assetRepository.save(asset);
        assetRepository.flush();

        if (asset.getAssignedPerson() != null) {
            emailService.sendMail(
                    asset.getAssignedPerson().getEmail(),
                    "Wartung beendet",
                    "Die Wartung für Ihr zugewiesenes Asset \"" + asset.getName() + "\" wurde beendet. Es steht zur Abholung bereit."
            );
        } else {
             // Fallback to Admin if no one assigned (e.g. pool asset or unassigned before repair)
             emailService.sendMail(
                adminMail,
                "Wartung beendet (Kein Besitzer)",
                "Die Wartung für das Asset \"" + asset.getName() + "\" wurde beendet."
            );
        }
        
        historyService.logEvent(asset, "MAINTENANCE_FINISHED", "Asset maintenance finished", (Person) null);
    }

    /**
     * Beendet speziell eine Reparaturwartung.
     *
     * @param assetUuid Die UUID des Assets
     */
    public void finishRepair(long assetUuid) {
        Asset asset = assetRepository.findByUuid(assetUuid)
                .orElseThrow(() -> new BokerfiException(
                        ErrorCode.ENTITY_DOES_NOT_EXIST,
                        ASSET_NOT_FOUND
                ));

        if (asset.getStatus() == AssetStatus.MAINTENANCE_REPAIR) {
             asset.setStatus(AssetStatus.READY_FOR_REPAIR_PICKUP);
        } else {
             throw new BokerfiException(ErrorCode.CONFLICT, "Asset is not in repair maintenance (Status: " + asset.getStatus() + ")");
        }

        assetRepository.save(asset);
        assetRepository.flush();

        if (asset.getAssignedPerson() != null) {
            emailService.sendMail(
                    asset.getAssignedPerson().getEmail(),
                    "Reparatur beendet",
                    "Die Reparatur für Ihr zugewiesenes Asset \"" + asset.getName() + "\" wurde beendet. Es steht zur Abholung bereit."
            );
        } else {
             emailService.sendMail(
                adminMail,
                "Reparatur beendet (Kein Besitzer)",
                "Die Reparatur für das Asset \"" + asset.getName() + "\" wurde beendet."
            );
        }

        historyService.logEvent(asset, "REPAIR_FINISHED", "Asset repair finished", (Person) null);
    }

    /**
     * Sondert ein Asset aus.
     *
     * @param assetUuid Die UUID des auszusondernden Assets
     */
    public void retireAsset(long assetUuid) {
        Asset asset = assetRepository.findByUuid(assetUuid)
                .orElseThrow(() -> new BokerfiException(
                        ErrorCode.ENTITY_DOES_NOT_EXIST,
                        ASSET_NOT_FOUND
                ));

        switch (asset.getStatus()) {
            case MAINTENANCE_FINAL, RETURNED_FINAL, IN_STOCK -> asset.setStatus(AssetStatus.RETIRED);
            case RETIRED -> {
                return; // Idempotent
            }
            default -> throw new BokerfiException(ErrorCode.CONFLICT, "Asset cannot be retired from state: " + asset.getStatus());
        }

        assetRepository.save(asset);
        assetRepository.flush();
        historyService.logEvent(asset, "RETIRED", "Asset retired", (Person) null);
    }

}
