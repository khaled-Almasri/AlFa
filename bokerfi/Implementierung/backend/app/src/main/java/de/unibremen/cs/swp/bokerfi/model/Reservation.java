package de.unibremen.cs.swp.bokerfi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Entität für eine Reservierung.
 * <p>
 * Speichert Zeiträume, in denen Assets oder Räume reserviert sind.
 * </p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Reservation extends DBEntity {

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @ManyToOne(optional = false)
    private Person person;

    @ManyToOne(optional = false)
    private Location location;

    @Column
    private String reason;
}
