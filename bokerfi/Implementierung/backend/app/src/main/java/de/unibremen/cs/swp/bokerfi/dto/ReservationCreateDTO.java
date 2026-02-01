package de.unibremen.cs.swp.bokerfi.dto;

import java.time.LocalDateTime;

/**
 * DTO zum Erstellen einer Reservierung.
 *
 * @param start        Der Startzeitpunkt
 * @param end          Der Endzeitpunkt
 * @param personUuid   Die UUID der Person
 * @param locationUuid Die UUID des Standorts/Raums
 */
public record ReservationCreateDTO(
        LocalDateTime start,
        LocalDateTime end,
        String personUuid,
        String locationUuid,
        String reason
) {}
