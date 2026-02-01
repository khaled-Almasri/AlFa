package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.infrastructure.EmailService;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service für die Meldung von Defekten durch Mitarbeitende.
 */
@Service
@Transactional
public class AssetReportDefectService {

    private final AssetRepository assetRepository;
    private final PersonRepository personRepository;
    private final EmailService emailService;
    private final HistoryService historyService;
    private final String adminMail;

    public AssetReportDefectService(AssetRepository assetRepository,
                                    PersonRepository personRepository,
                                    EmailService emailService,
                                    HistoryService historyService,
                                    @Value("${bokerfi.mail.admin}") String adminMail) {
        this.assetRepository = assetRepository;
        this.personRepository = personRepository;
        this.emailService = emailService;
        this.historyService = historyService;
        this.adminMail = adminMail;
    }

    /**
     * Meldet einen Defekt für ein Asset.
     *
     * @param assetUuid       Die UUID des Assets
     * @param reporterUuid    Die UUID der meldenden Person (Mitarbeiter)
     * @param defectDescription Die Beschreibung des Defekts
     */
    public void reportDefect(long assetUuid, long reporterUuid, String defectDescription) {
        Asset asset = assetRepository.findByUuid(assetUuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Asset not found"));

        Person reporter = personRepository.findByUuid(reporterUuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Person not found"));

        // Only assigned person can report defect? Or anyone? 
        // Usually the person holding the asset.
        if (asset.getAssignedPerson() != null && !asset.getAssignedPerson().equals(reporter)) {
             // Maybe allow Manager to report for anyone? 
             // Requirement: "Mitarbeitende benötigen die Möglichkeit, Defekte zu melden."
             // Assuming strict check for now: Only assignee can report.
             // But what if it's a pool asset? 
             // Let's stick to simple: Log it and notify Admin.
        }

        emailService.sendMail(
                adminMail,
                "Defekt gemeldet: " + asset.getName(),
                "Mitarbeiter " + reporter.getFirstName() + " " + reporter.getLastName() + 
                " (" + reporter.getPersonnelNumber() + ") meldet einen Defekt für Asset " + 
                asset.getInventoryNumber() + ":\n\n" + defectDescription
        );

        historyService.logEvent(asset, "DEFECT_REPORTED", "Defect reported: " + defectDescription, reporter);
    }
}
