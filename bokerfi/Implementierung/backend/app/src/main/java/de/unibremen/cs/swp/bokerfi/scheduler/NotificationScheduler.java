package de.unibremen.cs.swp.bokerfi.scheduler;

import de.unibremen.cs.swp.bokerfi.infrastructure.EmailService;
import de.unibremen.cs.swp.bokerfi.model.Assignment;
import de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Plant und führt regelmäßige Benachrichtigungen durch.
 */
@Component
@Slf4j
public class NotificationScheduler {

    private final AssignmentRepository assignmentRepository;
    private final EmailService emailService;

    private final de.unibremen.cs.swp.bokerfi.service.HistoryService historyService;
    private final de.unibremen.cs.swp.bokerfi.persistence.AssetRepository assetRepository;

    public NotificationScheduler(AssignmentRepository assignmentRepository, EmailService emailService, de.unibremen.cs.swp.bokerfi.service.HistoryService historyService, de.unibremen.cs.swp.bokerfi.persistence.AssetRepository assetRepository) {
        this.assignmentRepository = assignmentRepository;
        this.emailService = emailService;
        this.historyService = historyService;
        this.assetRepository = assetRepository;
    }

    /**
     * Prüft täglich (z.B. um 8:00 Uhr) auf überfällige oder bald fällige Rückgaben.
     */
    // @Scheduled(cron = "0 0 8 * * ?") // Production: 8 AM
    @Scheduled(fixedRate = 60000) // Dev: Every 1 minute for testing
    @org.springframework.transaction.annotation.Transactional
    public void convertAndSendOverdueNotifications() {
        log.info("Running notification scheduler...");
        
        // 1. Check for assignments overdue (Deadline < Now)
        List<Assignment> overdue = assignmentRepository.findByReturnDeadlineBeforeAndReturnedAtIsNull(LocalDateTime.now());
        log.info("Found {} overdue assignments.", overdue.size());

        for (Assignment assignment : overdue) {
            de.unibremen.cs.swp.bokerfi.model.Asset asset = assignment.getAsset();
            log.info("Processing overdue assignment {}: Asset {} Status is {}", assignment.getId(), asset.getName(), asset.getStatus());

            // SYSTEM ACTION: MARK_FOR_FINAL_RETURN
            // Only if still ASSIGNED or READY_FOR_INITIAL_PICKUP.
            if (asset.getStatus() == de.unibremen.cs.swp.bokerfi.model.AssetStatus.ASSIGNED || 
                asset.getStatus() == de.unibremen.cs.swp.bokerfi.model.AssetStatus.READY_FOR_INITIAL_PICKUP) {
                 
                 asset.setStatus(de.unibremen.cs.swp.bokerfi.model.AssetStatus.WAITING_FOR_FINAL_RETURN);
                 assetRepository.save(asset);
                 
                 historyService.logEvent(asset, "MARKED_FOR_FINAL_RETURN", "System auto-marked for final return (Overdue)", (de.unibremen.cs.swp.bokerfi.model.Person) null);
                 log.info("Asset {} auto-marked for final return.", asset.getUuid());
            }

            // Notification
            sendNotification(assignment, "Rückgabefrist abgelaufen: " + asset.getName(),
                    "Die Rückgabefrist für das Asset '" + asset.getName() + "' ist abgelaufen (" + assignment.getReturnDeadline() + "). Eine Rückgabe ist nicht mehr möglich. Bitte kontaktieren Sie den Administrator.");
        }
        
        // 2. Check for assignments expiring soon (Deadline between Now and Now + 3 Days)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soon = now.plusDays(3);
        List<Assignment> expiring = assignmentRepository.findByReturnDeadlineBetweenAndReturnedAtIsNull(now, soon);
        for (Assignment assignment : expiring) {
            // Only notify, do not change status yet
            sendNotification(assignment, "Rückgabefrist läuft ab: " + assignment.getAsset().getName(),
                    "Die Rückgabefrist für das Asset '" + assignment.getAsset().getName() + "' läuft am " + assignment.getReturnDeadline() + " ab.");
        }
    }

    private void sendNotification(Assignment assignment, String subject, String body) {
        try {
            String recipient = assignment.getPerson().getEmail();
            emailService.sendMail(recipient, subject, body);
            log.info("Sent notification '{}' to {}", subject, recipient);
        } catch (Exception e) {
            log.error("Failed to send notification for assignment {}", assignment.getId(), e);
        }
    }
}
