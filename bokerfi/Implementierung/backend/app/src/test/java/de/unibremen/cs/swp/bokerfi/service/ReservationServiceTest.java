package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.ReservationCreateDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Location;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.model.Reservation;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import de.unibremen.cs.swp.bokerfi.persistence.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private ReservationService service;

    @Test
    void create_ShouldThrowException_WhenStartOrEndIsNull() {
        // Case 1: Start null
        ReservationCreateDTO dto1 = new ReservationCreateDTO(null, LocalDateTime.now(), "1", "2", null);
        BokerfiException ex1 = assertThrows(BokerfiException.class, () -> service.create(dto1));
        assertEquals(ErrorCode.CONSTRAINT_VIOLATION, ex1.getErrorCode());

        // Case 2: End null
        ReservationCreateDTO dto2 = new ReservationCreateDTO(LocalDateTime.now(), null, "1", "2", null);
        BokerfiException ex2 = assertThrows(BokerfiException.class, () -> service.create(dto2));
        assertEquals(ErrorCode.CONSTRAINT_VIOLATION, ex2.getErrorCode());
    }

    @Test
    void create_ShouldThrowException_WhenEndIsBeforeStart() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now(); // END before START
        ReservationCreateDTO dto = new ReservationCreateDTO(start, end, "1", "2", null);

        BokerfiException ex = assertThrows(BokerfiException.class, () -> service.create(dto));
        assertEquals(ErrorCode.CONSTRAINT_VIOLATION, ex.getErrorCode());
    }

    @Test
    void create_ShouldThrowException_WhenPersonNotFound() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        ReservationCreateDTO dto = new ReservationCreateDTO(start, end, "999", "2", null);

        when(personRepository.findByUuid(999L)).thenReturn(Optional.empty());

        BokerfiException ex = assertThrows(BokerfiException.class, () -> service.create(dto));
        assertEquals(ErrorCode.ENTITY_DOES_NOT_EXIST, ex.getErrorCode());
        assertEquals("Person not found", ex.getMessage());
    }

    @Test
    void create_ShouldThrowException_WhenLocationNotFound() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        ReservationCreateDTO dto = new ReservationCreateDTO(start, end, "1", "888", null);

        Person mockPerson = new Person();
        when(personRepository.findByUuid(1L)).thenReturn(Optional.of(mockPerson));
        when(locationRepository.findByUuid(888L)).thenReturn(Optional.empty());

        BokerfiException ex = assertThrows(BokerfiException.class, () -> service.create(dto));
        assertEquals(ErrorCode.ENTITY_DOES_NOT_EXIST, ex.getErrorCode());
        assertEquals("Location not found", ex.getMessage());
    }

    @Test
    @SuppressWarnings("null")
    void create_ShouldCreateReservation_WhenValid() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        ReservationCreateDTO dto = new ReservationCreateDTO(start, end, "10", "20", "Reason");

        Person mockPerson = new Person();
        Location mockLocation = new Location();

        when(personRepository.findByUuid(10L)).thenReturn(Optional.of(mockPerson));
        when(locationRepository.findByUuid(20L)).thenReturn(Optional.of(mockLocation));
        
        // Mock save to populate UUID on the passed entity (optional simulation)
        // or just verify logic. The service calls repository.save(reservation).
        // Since we mock repository, we can capture the argument.
        
        service.create(dto);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(repository).save(captor.capture());

        Reservation saved = captor.getValue();
        assertEquals(start, saved.getStart());
        assertEquals(end, saved.getEnd());
        assertEquals(mockPerson, saved.getPerson());
        assertEquals(mockLocation, saved.getLocation());
        assertEquals("Reason", saved.getReason());
        
        assertNotNull(saved); // basic check
    }
    
    @Test
    @SuppressWarnings("null")
    void create_ShouldWork_WithNullPersonAndLocation() {
        // Service logic permits null UUIDs in DTO, which results in null entities in the reservation
        
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        ReservationCreateDTO dto = new ReservationCreateDTO(start, end, null, null, null);

        service.create(dto);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(repository).save(captor.capture());
        
        Reservation saved = captor.getValue();
        assertEquals(start, saved.getStart());
        assertNull(saved.getPerson());
        assertNull(saved.getLocation());
    }
}
