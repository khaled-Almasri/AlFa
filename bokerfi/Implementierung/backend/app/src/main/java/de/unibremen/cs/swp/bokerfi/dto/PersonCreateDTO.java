package de.unibremen.cs.swp.bokerfi.dto;

import de.unibremen.cs.swp.bokerfi.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * DTO zum Erstellen einer neuen Person.
 *
 * @param email           Die E-Mail-Adresse
 * @param firstName       Der Vorname
 * @param lastName        Der Nachname
 * @param personnelNumber Die Personalnummer
 * @param password        Das Passwort (Klartext)
 * @param role            Die primäre Rolle
 * @param roles           Liste der Rollen (optional)
 */
public record PersonCreateDTO(
        @NotNull
        @Email
        @JsonAlias({"username", "emailAddress"})
        String email,

        @NotBlank
        @JsonAlias({"firstname", "firstName"})
        String firstName,

        @NotBlank
        @JsonAlias({"lastname", "lastName"})
        String lastName,

        @NotBlank
        @NotBlank
        @JsonAlias({"pNr", "personnelNo", "employeeId", "personnelNumber", "employeeNumber"})
        String personnelNumber,

        @NotBlank
        String password,

        Role role,

        @JsonAlias("roles")
        java.util.List<Role> roles
) {}
