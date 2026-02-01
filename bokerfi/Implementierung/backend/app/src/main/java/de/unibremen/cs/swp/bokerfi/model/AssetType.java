package de.unibremen.cs.swp.bokerfi.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entität für einen Asset-Typ.
 * <p>
 * Definiert eine Kategorie von Assets (z.B. Laptop, Werkzeug).
 * </p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AssetType extends DBEntity {

    // Kategorie / Typ (Laptop, Raum, Werkzeug)
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description = "";
}
