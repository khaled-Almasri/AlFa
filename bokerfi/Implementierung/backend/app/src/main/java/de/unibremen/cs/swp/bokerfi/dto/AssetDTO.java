package de.unibremen.cs.swp.bokerfi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;

/**
 * DTO, das ein Asset repräsentiert.
 *
 * @param uuid            Die eindeutige Kennung des Assets
 * @param name            Der Name des Assets
 * @param status          Der aktuelle Status des Assets
 * @param condition       Der Zustand des Assets
 * @param typeName        Der Name des Asset-Typs
 * @param inventoryNumber Die Inventarnummer
 * @param purchaseDate    Das Kaufdatum
 * @param warrantyEnd     Das Enddatum der Garantie
 * @param assigneeUuid    Die UUID der zugewiesenen Person (falls vorhanden)
 * @param locationUuid    Die UUID des Standorts
 * @param typeUuid        Die UUID des Asset-Typs
 * @param location        Das vollständige Standort-DTO
 * @param type            Das vollständige Asset-Typ-DTO
 */
public record AssetDTO(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        long uuid,
        String name,
        AssetStatus status,
        String condition,
        String typeName,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String inventoryNumber,
        java.time.LocalDate purchaseDate,
        java.time.LocalDate warrantyEnd,
        String assigneeUuid,
        String locationUuid,
        String typeUuid,
        LocationDTO location,
        AssetTypeDTO type,
        PersonDTO assignedPerson,
        java.time.LocalDateTime addedAt,
        java.time.LocalDateTime returnDeadline,
        java.time.LocalDateTime assignedAt
) {
    public AssetDTO {
        if (status == null) {
            status = AssetStatus.IN_STOCK;
        }
    }

    /**
     * Gibt die ID (Alias für uuid) für Frontend-Kompatibilität zurück.
     *
     * @return Die UUID als long
     */
    @JsonProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public long getId() {
        return uuid;
    }


}
