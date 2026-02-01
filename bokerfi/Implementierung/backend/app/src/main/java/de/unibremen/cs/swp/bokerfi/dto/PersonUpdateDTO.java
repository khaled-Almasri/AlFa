package de.unibremen.cs.swp.bokerfi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * DTO zum Aktualisieren einer Person.
 *
 * @param firstName       Der neue Vorname
 * @param lastName        Der neue Nachname
 * @param email           Die neue E-Mail-Adresse
 * @param personnelNumber Die neue Personalnummer
 */
public record PersonUpdateDTO(

        @JsonAlias({"firstname", "firstName"})
        String firstName,

        @JsonAlias({"lastname", "lastName"})
        String lastName,

        @JsonAlias({"emailAddress", "email"})
        String email,

        @JsonAlias({"pNr", "personnelNo", "employeeId", "personnelNumber", "employeeNumber", "id"})
        String personnelNumber,

        @JsonAlias({"role", "userRole"})
        de.unibremen.cs.swp.bokerfi.model.Role role
) {}
