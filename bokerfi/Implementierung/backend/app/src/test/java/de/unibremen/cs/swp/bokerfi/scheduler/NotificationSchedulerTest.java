package de.unibremen.cs.swp.bokerfi.scheduler;

import de.unibremen.cs.swp.bokerfi.infrastructure.EmailService;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.Assignment;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationSchedulerTest {

    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private de.unibremen.cs.swp.bokerfi.service.HistoryService historyService;
    @Mock
    private de.unibremen.cs.swp.bokerfi.persistence.AssetRepository assetRepository;

    private NotificationScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new NotificationScheduler(assignmentRepository, emailService, historyService, assetRepository);
    }

    @Test
    void convertAndSendOverdueNotifications_SendsEmailWrapper() {
        // Arrange
        Assignment overdue = new Assignment();
        overdue.setId(1L);
        Asset asset = new Asset();
        asset.setName("Laptop");
        asset.setStatus(de.unibremen.cs.swp.bokerfi.model.AssetStatus.ASSIGNED); // Critical for system mark
        overdue.setAsset(asset);
        Person person = new Person();
        person.setEmail("user@test.de");
        overdue.setPerson(person);
        overdue.setReturnDeadline(LocalDateTime.now().minusDays(1));

        when(assignmentRepository.findByReturnDeadlineBeforeAndReturnedAtIsNull(any())).thenReturn(List.of(overdue));
        when(assignmentRepository.findByReturnDeadlineBetweenAndReturnedAtIsNull(any(), any())).thenReturn(Collections.emptyList());

        // Act
        scheduler.convertAndSendOverdueNotifications();

        // Assert
        verify(emailService).sendMail(eq("user@test.de"), anyString(), anyString());
        // Verify Asset Status Update
        verify(assetRepository).save(asset);
        assert(asset.getStatus() == de.unibremen.cs.swp.bokerfi.model.AssetStatus.WAITING_FOR_FINAL_RETURN);
    }
    
    @Test
    void convertAndSendOverdueNotifications_ExpiringSoon() {
        // Arrange
        Assignment expiring = new Assignment();
        expiring.setId(2L);
        Asset asset = new Asset();
        asset.setName("Beamer");
        expiring.setAsset(asset);
        Person person = new Person();
        person.setEmail("user2@test.de");
        expiring.setPerson(person);
        expiring.setReturnDeadline(LocalDateTime.now().plusDays(1));

        when(assignmentRepository.findByReturnDeadlineBeforeAndReturnedAtIsNull(any())).thenReturn(Collections.emptyList());
        when(assignmentRepository.findByReturnDeadlineBetweenAndReturnedAtIsNull(any(), any())).thenReturn(List.of(expiring));

        // Act
        scheduler.convertAndSendOverdueNotifications();

        // Assert
        verify(emailService).sendMail(eq("user2@test.de"), anyString(), anyString());
    }

    @Test
    void convertAndSendOverdueNotifications_HandlesException() {
        // Arrange
        Assignment overdue = new Assignment();
        overdue.setId(1L);
        Asset asset = new Asset();
        asset.setName("Laptop");
        asset.setStatus(de.unibremen.cs.swp.bokerfi.model.AssetStatus.ASSIGNED);
        overdue.setAsset(asset);
        Person person = new Person();
        person.setEmail("error@test.de");
        overdue.setPerson(person);

        when(assignmentRepository.findByReturnDeadlineBeforeAndReturnedAtIsNull(any())).thenReturn(List.of(overdue));
        doThrow(new RuntimeException("Mail failed")).when(emailService).sendMail(anyString(), anyString(), anyString());

        // Act
        scheduler.convertAndSendOverdueNotifications();

        // Assert - should not crash
        verify(emailService).sendMail(anyString(), anyString(), anyString());
    }
}
