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
 * Service zum Starten der Wartung von Assets.
 * <p>
 * Handhabt den Übergang von Assets in den Wartungsmodus (Reparatur oder final).
 * </p>
 */
@Service
@org.springframework.transaction.annotation.Transactional
public class AssetMaintenanceStartService {

    private final AssetRepository assetRepository;
    private final EmailService emailService;
    private final HistoryService historyService;
    private final String adminMail;

    /**
     * Erstellt einen neuen AssetMaintenanceStartService.
     *
     * @param assetRepository Das Asset-Repository
     * @param emailService    Der E-Mail-Service
     * @param adminMail       Die Admin-E-Mail-Adresse
     */
    public AssetMaintenanceStartService(AssetRepository assetRepository,
                                        EmailService emailService,
                                        HistoryService historyService,
                                        @Value("${bokerfi.mail.admin}") String adminMail) {
        this.assetRepository = assetRepository;
        this.emailService = emailService;
        this.historyService = historyService;
        this.adminMail = adminMail;
    }

    /**
     * Startet die Wartung für ein Asset.
     *
     * @param assetUuid Die UUID des Assets
     */
    public void startMaintenance(long assetUuid) {

        Asset asset = assetRepository.findByUuid(assetUuid)
                .orElseThrow(() -> new BokerfiException(
                        ErrorCode.ENTITY_DOES_NOT_EXIST,
                        "Asset not found"
                ));

        switch (asset.getStatus()) {
            case RETURNED_REPAIR -> asset.setStatus(AssetStatus.MAINTENANCE_REPAIR);
            case RETURNED_FINAL -> asset.setStatus(AssetStatus.MAINTENANCE_FINAL);
            default -> throw new BokerfiException(ErrorCode.CONFLICT, "Asset must be returned (Repair/Final) before starting maintenance");
        }

        assetRepository.save(asset);

        emailService.sendMail(
                adminMail,
                "Asset in Wartung",
                "Das Asset \"" + asset.getName() + "\" befindet sich nun in Wartung."
        );
        
        historyService.logEvent(asset, "MAINTENANCE_STARTED", "Asset maintenance started", (Person) null);
    }
}
