package de.unibremen.cs.swp.bokerfi.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entität für ein Asset (Gegenstand).
 * <p>
 * Stellt einen physischen Gegenstand dar, der verwaltet wird.
 * </p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Asset extends DBEntity {

    @Column(nullable = false, unique = true)
    private String inventoryNumber;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private AssetType type;

    @ManyToOne(optional = false)
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status = AssetStatus.IN_STOCK;

    @Column
    private java.time.LocalDate purchaseDate;

    @Column
    private java.time.LocalDate warrantyEnd;

    // Aktuelle Zuweisung (redundant zur Assignment-Historie, aber praktischer Zugriff)
    // Kann null sein, wenn Status IN_STOCK, RETIRED, MAINTENANCE, etc.
    @ManyToOne
    @JoinColumn(name = "assigned_person_id")
    private Person assignedPerson;

    @Column(name = "asset_condition")
    private String condition;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<Assignment> assignments;

    @Column
    private java.time.LocalDateTime addedAt;

}
