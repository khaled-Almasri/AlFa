package de.unibremen.cs.swp.bokerfi.controller;

import de.unibremen.cs.swp.bokerfi.dto.ReservationCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.ReservationDTO;
import de.unibremen.cs.swp.bokerfi.service.ReservationCreateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * REST-Controller für Reservierungen.
 */
@RestController
public class ReservationController {

    private final ReservationCreateService reservationCreateService;

    public ReservationController(ReservationCreateService reservationCreateService) {
        this.reservationCreateService = reservationCreateService;
    }

    /**
     * Erstellt eine neue Reservierung.
     *
     * @param dto Reservierungsdaten (Zeitraum, Person-UUID, Raum-UUID)
     * @return erstellte Reservierung
     */
    @PostMapping("/api/reservations")
    public ResponseEntity<ReservationDTO> createReservation(
            @Valid @RequestBody ReservationCreateDTO dto
    ) {

        ReservationDTO created =
                reservationCreateService.createReservation(dto);

        return ResponseEntity
                .created(URI.create("/api/reservations/" + created.uuid()))
                .body(created);
    }
}
