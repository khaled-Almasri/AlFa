package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;
import de.unibremen.cs.swp.bokerfi.model.Assignment;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import de.unibremen.cs.swp.bokerfi.infrastructure.EmailService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service für die Zuweisung von Assets an Personen.
 * <p>
 * Handhabt den Zuweisungsprozess, einschließlich Validierung und E-Mail-Benachrichtigung.
 * </p>
 */
@Service
@org.springframework.transaction.annotation.Transactional
public class AssetAssignService {

    private final AssetRepository assetRepository;
    private final PersonRepository personRepository;
    private final AssignmentRepository assignmentRepository;
    private final EmailService emailService;
    private final HistoryService historyService;

    /**
     * Erstellt einen neuen AssetAssignService.
     *
     * @param assetRepository      Das Asset-Repository
     * @param personRepository     Das Personen-Repository
     * @param assignmentRepository Das Zuweisungs-Repository
     * @param emailService         Der E-Mail-Service
     */
    public AssetAssignService(
            AssetRepository assetRepository,
            PersonRepository personRepository,
            AssignmentRepository assignmentRepository,
            EmailService emailService,
            HistoryService historyService
    ) {
        this.assetRepository = assetRepository;
        this.personRepository = personRepository;
        this.assignmentRepository = assignmentRepository;
        this.emailService = emailService;
        this.historyService = historyService;
    }

    /**
     * Weist ein Asset einer Person zu.
     *
     * @param assetUuid  Die UUID des Assets
     * @param personUuid Die UUID der Person
     * @return Die UUID des zugewiesenen Assets
     */
    public long assign(long assetUuid, long personUuid, LocalDateTime returnDeadline) {
        Asset asset = assetRepository.findByUuid(assetUuid)
                .or(() -> assetRepository.findById(assetUuid))
                .orElseThrow(() -> new BokerfiException(
                        ErrorCode.ENTITY_DOES_NOT_EXIST,
                        "Asset not found"
                ));

        if (asset.getStatus() != AssetStatus.IN_STOCK && asset.getStatus() != AssetStatus.RETURNED_FINAL) {
             // ... kept logic ...
            throw new BokerfiException(
                    ErrorCode.ASSET_NOT_AVAILABLE,
                    "Asset is not in IN_STOCK or RETURNED_FINAL status. Status is: " + asset.getStatus()
            );
        }

        Person person = personRepository.findByUuid(personUuid)
                .or(() -> personRepository.findById(personUuid))
                .orElseThrow(() -> new BokerfiException(
                        ErrorCode.ENTITY_DOES_NOT_EXIST,
                        "Person not found"
                ));

        asset.setAssignedPerson(person);
        asset.setStatus(AssetStatus.READY_FOR_INITIAL_PICKUP);

        // Create Assignment record
        Assignment assignment = new Assignment();
        assignment.setAsset(asset);
        assignment.setPerson(person);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setReturnDeadline(returnDeadline);
        
        assignmentRepository.save(assignment);
        assetRepository.save(asset);
        assetRepository.flush();

        emailService.sendMail(
                person.getEmail(),
                "Asset zugewiesen",
                "Ihnen wurde das Asset \"" + asset.getName() + "\" zugewiesen."
        );

        historyService.logEvent(asset, "ASSIGNMENT_INITIATED", "Asset assigned to person " + person.getPersonnelNumber(), (Person) null);
        
        return asset.getUuid();
    }

    public void extendDeadline(long assetUuid, LocalDateTime newDeadline) {
        Asset asset = assetRepository.findByUuid(assetUuid)
                .or(() -> assetRepository.findById(assetUuid))
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Asset not found"));

        if (asset.getAssignments() == null) {
            throw new BokerfiException(ErrorCode.ASSET_NOT_AVAILABLE, "No assignments found");
        }

        Assignment active = asset.getAssignments().stream()
                .filter(a -> a.getReturnedAt() == null)
                .findFirst()
                .orElseThrow(() -> new BokerfiException(ErrorCode.ASSET_NOT_AVAILABLE, "No active assignment found"));

        if (active.getReturnDeadline() != null && active.getReturnDeadline().isBefore(LocalDateTime.now())) {
             throw new BokerfiException(ErrorCode.CONFLICT, "Cannot extend expired deadline. Asset should be returned.");
        }

        active.setReturnDeadline(newDeadline);
        assignmentRepository.save(active);
        
        historyService.logEvent(asset, "DEADLINE_EXTENDED", "Deadline extended to " + newDeadline, active.getPerson());

        emailService.sendMail(
                active.getPerson().getEmail(),
                "Rückgabefrist verlängert: " + asset.getName(),
                "Die Rückgabefrist für Ihr Asset '" + asset.getName() + "' wurde bis zum " + newDeadline + " verlängert."
        );
    }
}
