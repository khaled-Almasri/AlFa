package de.unibremen.cs.swp.bokerfi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Protokolliert alle relevanten Ereignisse im Lebenszyklus eines Assets.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class AssetHistory extends DBEntity {

    @ManyToOne(optional = false)
    private Asset asset;

    /**
     * Die Person, die die Aktion ausgelöst hat (null bei Systemaktionen).
     */
    @ManyToOne
    private Person actor;

    @Column(nullable = false)
    private String eventType;

    @Column(length = 1024)
    private String description;

    @Column(nullable = false)
    private LocalDateTime timestamp;

}
