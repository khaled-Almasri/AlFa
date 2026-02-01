package de.unibremen.cs.swp.bokerfi.dto;

// Asset anlegen
import com.fasterxml.jackson.annotation.JsonAlias;



/**
 * DTO zum Erstellen oder Aktualisieren eines Assets.
 *
 * @param name            Der Name des Assets
 * @param inventoryNumber Die Inventarnummer
 * @param typeUuid        Die UUID des Asset-Typs
 * @param locationUuid    Die UUID des Standorts
 * @param purchaseDate    Das Kaufdatum des Assets
 * @param warrantyEnd     Das Enddatum der Garantie
 * @param condition       Der Zustand des Assets
 */
public record AssetCreateDTO(
        String name,
        String inventoryNumber,
        @JsonAlias({"assetTypeUuid", "assetTypeId", "typeId", "assetType", "type"}) String typeUuid,
        @JsonAlias({"locationId", "location"}) String locationUuid,
        java.time.LocalDate purchaseDate,
        java.time.LocalDate warrantyEnd,
        String condition
) {}
