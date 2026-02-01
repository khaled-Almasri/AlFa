package de.unibremen.cs.swp.bokerfi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.unibremen.cs.swp.bokerfi.model.Role;

/**
 * DTO, das eine Person (Benutzer) repräsentiert.
 *
 * @param email           Die E-Mail-Adresse
 * @param firstName       Der Vorname
 * @param lastName        Der Nachname
 * @param personnelNumber Die Personalnummer
 * @param role            Die primäre Rolle
 * @param active          Ob der Benutzer aktiv ist
 * @param uuid            Die eindeutige Kennung
 * @param roles           Liste der zugewiesenen Rollen
 */
public record PersonDTO(
        String email,
        String firstName,
        String lastName,
        String personnelNumber,
        Role role,
        boolean active,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Long uuid,
        Long version,
        java.util.List<String> roles
) {
    /**
     * Gibt die ID (Alias für uuid) für Frontend-Kompatibilität zurück.
     *
     * @return Die UUID als long
     */
    @com.fasterxml.jackson.annotation.JsonProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Long getId() {
        return uuid;
    }
}
