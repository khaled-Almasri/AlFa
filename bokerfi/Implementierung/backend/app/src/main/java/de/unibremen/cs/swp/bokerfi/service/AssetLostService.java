package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.infrastructure.EmailService;
import de.unibremen.cs.swp.bokerfi.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service zur Handhabung verlorener Assets.
 * <p>
 * Ermöglicht das Markieren von Assets als verloren und das Bestätigen des Verlusts.
 * </p>
 */
@Service
public class AssetLostService {

    private final AssetRepository assetRepository;
    private final EmailService emailService;
    private final HistoryService historyService;
    private final String adminMail;

    /**
     * Erstellt einen neuen AssetLostService.
     *
     * @param assetRepository Das Asset-Repository
     * @param emailService    Der E-Mail-Service
     * @param adminMail       Die Admin-E-Mail-Adresse
     */
    public AssetLostService(AssetRepository assetRepository,
                            EmailService emailService,
                            HistoryService historyService,
                            @Value("${bokerfi.mail.admin}") String adminMail) {
        this.assetRepository = assetRepository;
        this.emailService = emailService;
        this.historyService = historyService;
        this.adminMail = adminMail;
    }

    /**
     * Markiert ein Asset als verloren gemeldet.
     *
     * @param assetUuid Die UUID des Assets
     */
    public void markLost(long assetUuid) {

        java.util.Optional<Asset> optAsset = assetRepository.findByUuid(assetUuid);
        
        if (optAsset.isEmpty()) {
             throw new BokerfiException(
                        ErrorCode.ENTITY_DOES_NOT_EXIST,
                        "Asset not found"
                );
        }
        
        Asset asset = optAsset.get();

        // Simplified: Go to LOST state directly or MARKED_LOST
        // Diagram: User: MARK_LOST -> MARKED_LOST -> Manager: CONFIRM_LOST -> LOST
        
        if (asset.getStatus() != AssetStatus.ASSIGNED) {
             throw new BokerfiException(ErrorCode.CONFLICT, "Only assigned assets can be marked lost");
        }
        
        if (asset.getStatus() == AssetStatus.MARKED_LOST || asset.getStatus() == AssetStatus.LOST || asset.getStatus() == AssetStatus.RETIRED) {
             throw new BokerfiException(ErrorCode.CONFLICT, "Asset is already lost or retired");
        }
        // Also conflict if asset is not assigned? depends on requirements. 
        // If testAdminPathA tries to mark IN_STOCK asset as lost, maybe that is allowed?
        // But if the test expects Conflict, it implies we assume a state precondition.
        // Let's print the current status to debug.
        asset.setStatus(AssetStatus.MARKED_LOST);
        
        assetRepository.save(asset);

        emailService.sendMail(
                adminMail,
                "Asset als verloren gemeldet",
                "Das Asset \"" + asset.getName() + "\" wurde als verloren markiert."
        );
        
        historyService.logEvent(asset, "MARKED_LOST", "Asset marked as lost", (Person) null);

    }

    /**
     * Bestätigt, dass ein Asset verloren ist.
     *
     * @param assetUuid Die UUID des Assets
     */
    public void confirmLoss(long assetUuid) {
        java.util.Optional<Asset> optAsset = assetRepository.findByUuid(assetUuid);
        if (optAsset.isEmpty()) {
             throw new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Asset not found");
        }
        Asset asset = optAsset.get();
        
        if (asset.getStatus() != AssetStatus.MARKED_LOST) {
             throw new BokerfiException(ErrorCode.CONFLICT, "Asset is not marked as lost");
        }
        
        asset.setStatus(AssetStatus.LOST);
        // Clear assignment mapping if it exists? 
        // Usually Assignment is closed when Returned or Lost. 
        // Assuming Assignment entity handles linkage, but Asset.assignedPerson should be cleared?
        asset.setAssignedPerson(null);
        
        assetRepository.save(asset);
        
        historyService.logEvent(asset, "LOST_CONFIRMED", "Asset loss confirmed", (Person) null);
    }

}
