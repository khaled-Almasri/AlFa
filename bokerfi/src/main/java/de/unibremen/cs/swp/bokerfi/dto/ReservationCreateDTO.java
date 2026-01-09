package de.unibremen.cs.swp.bokerfi.dto;

/**
 * DTO zum Erstellen einer neuen Reservierung.
 * Enthält nur die UUIDs der referenzierten Entitäten.
 */
public record ReservationCreateDTO(
        String timeSlot,
        Long personUuid,
        Long facilityUuid
) {
}
