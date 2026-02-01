package de.unibremen.cs.swp.bokerfi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Entität zur Repräsentation einer Person (Benutzer).
 */
@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Person extends DBEntity {

    @Column(nullable = false)
    @NonNull
    private String firstName = "";

    @Column(nullable = false)
    @NonNull
    private String lastName = "";

    @Column(nullable = false, unique = true)
    @NonNull
    private String email = "";

    @Column(nullable = false, unique = true)
    @NonNull
    private String personnelNumber = "";

    @Column(nullable = false)
    @NonNull
    private String password = ""; // Should be encoded

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.EMPLOYEE;

    @Column(nullable = false)
    private boolean active = true;
}