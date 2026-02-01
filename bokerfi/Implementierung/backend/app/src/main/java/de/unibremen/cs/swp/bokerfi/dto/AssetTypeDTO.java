package de.unibremen.cs.swp.bokerfi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO, das einen Asset-Typ repräsentiert.
 *
 * @param uuid        Die eindeutige Kennung
 * @param name        Der Name des Asset-Typs
 * @param description Die Beschreibung des Asset-Typs
 */
public record AssetTypeDTO(
        long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        long uuid,
        String name,
        String description
) {
    /**
     * Gibt die technische ID für Frontend-Kompatibilität zurück.
     *
     * @return Die ID als long
     */
    @JsonProperty("id")
    public long getId() {
        return id;
    }
}
