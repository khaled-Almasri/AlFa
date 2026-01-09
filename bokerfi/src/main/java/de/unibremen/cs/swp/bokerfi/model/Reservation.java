package de.unibremen.cs.swp.bokerfi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Reservation extends DBEntity {

    /**
     * Die Person zu dieser Reservierung.
     */
    @ManyToOne
    private Person person;

    /**
     * Der Raum zu dieser Reservierung.
     */
    @ManyToOne
    private Facility facility;

    /**
     * Der Reservierungszeitraum.
     */
    @NotBlank
    private String timeSlot;
}
