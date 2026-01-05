package de.unibremen.cs.swp.bokerfi.dto;

/**
 * DTO zur Ausgabe einer Person über die REST-API.
 */
public record PersonDTO(
        Long uuid,
        String firstName,
        String lastName,
        String email
) {
}
