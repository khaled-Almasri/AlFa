package de.unibremen.cs.swp.bokerfi.dto;

// Asset-Typ anlegen
/**
 * DTO zum Erstellen oder Aktualisieren eines Asset-Typs.
 *
 * @param name        Der Name des Asset-Typs
 * @param description Eine Beschreibung des Asset-Typs
 */
public record AssetTypeCreateDTO(
        String name,
        String description
) {}
