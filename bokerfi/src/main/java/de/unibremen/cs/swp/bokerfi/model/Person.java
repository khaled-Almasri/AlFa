package de.unibremen.cs.swp.bokerfi.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Entität zur Repräsentation einer Person.
 */
@Entity
@Getter
@Setter
@ToString
public class Person extends DBEntity {

    /**
     * Vorname der Person.
     */
    @NonNull
    private String firstName;

    /**
     * Nachname der Person.
     */
    @NonNull
    private String lastName;

    /**
     * E-Mail-Adresse der Person.
     */
    @NonNull
    private String email;
}