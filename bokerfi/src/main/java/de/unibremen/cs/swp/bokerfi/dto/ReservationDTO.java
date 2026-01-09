package de.unibremen.cs.swp.bokerfi.dto;

/**
 * DTO zur Rückgabe von Reservierungen.
 */
public record ReservationDTO(
        String timeSlot,
        Long personUuid,
        Long facilityUuid,
        Long uuid
) {
}
