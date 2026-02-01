package de.unibremen.cs.swp.bokerfi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO, das einen Standort repräsentiert.
 *
 * @param uuid Die eindeutige Kennung
 * @param name Der Name des Standorts
 */
public record LocationDTO(
        long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        long uuid,
        String name
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
