package de.unibremen.cs.swp.bokerfi.dto;

/**
 * DTO zum Erstellen oder Aktualisieren eines Standorts.
 *
 * @param name Der Name des Standorts
 */
public record LocationCreateDTO(
        String name
) {}
