package de.unibremen.cs.swp.bokerfi.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Facility extends DBEntity {

    @NonNull
    private String name;
}
