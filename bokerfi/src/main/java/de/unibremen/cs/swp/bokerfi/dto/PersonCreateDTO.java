package de.unibremen.cs.swp.bokerfi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO für das Erstellen einer neuen Person.
 * Enthält nur die vom Client gelieferten Daten.
 */
public record PersonCreateDTO(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotNull
        @Email
        String email
) {}

