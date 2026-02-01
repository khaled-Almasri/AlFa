package de.unibremen.cs.swp.bokerfi.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Lebenszyklus-Zustände eines Assets (gemäß Diagramm).
 */
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AssetStatus {
    IN_STOCK,
    READY_FOR_INITIAL_PICKUP,
    ASSIGNED,
    MARKED_LOST,
    LOST,
    WAITING_FOR_REPAIR_RETURN,
    WAITING_FOR_FINAL_RETURN,
    RETURNED_REPAIR,
    RETURNED_FINAL,
    MAINTENANCE_REPAIR,
    MAINTENANCE_FINAL,
    READY_FOR_REPAIR_PICKUP,
    RETIRED;

    @com.fasterxml.jackson.annotation.JsonValue
    public String toValue() {
        return name();
    }
}