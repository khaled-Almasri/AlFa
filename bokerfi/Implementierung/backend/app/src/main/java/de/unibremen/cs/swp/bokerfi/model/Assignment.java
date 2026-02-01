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
 * Protokolliert die Zuweisung eines Assets an eine Person.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Assignment extends DBEntity {

    @ManyToOne(optional = false)
    private Asset asset;

    @ManyToOne(optional = false)
    private Person person;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    private LocalDateTime returnDeadline;

    private LocalDateTime returnedAt;

}
