package de.unibremen.cs.swp.bokerfi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstrakte Basisklasse für alle Datenbank-Entitäten.
 * <p>
 * Enthält einen technischen Primärschlüssel (id) sowie
 * eine fachliche UUID (TSID) zur eindeutigen Identifikation.
 * </p>
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class DBEntity {


    /**
     * Technischer Primärschlüssel, der vom DBMS generiert wird.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;

    /**
     * Fachliche UUID (TSID), eindeutig und nicht null.
     */
    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected Long uuid = TSID.Factory.getTsid().toLong();

    @Version
    @Column(nullable = false)
    private Long version;

}
