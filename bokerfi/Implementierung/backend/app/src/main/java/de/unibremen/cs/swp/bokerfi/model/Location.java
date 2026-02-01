package de.unibremen.cs.swp.bokerfi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entität für einen Standort.
 * <p>
 * Repräsentiert einen Ort, an dem sich Assets befinden können.
 * </p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Location extends DBEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
