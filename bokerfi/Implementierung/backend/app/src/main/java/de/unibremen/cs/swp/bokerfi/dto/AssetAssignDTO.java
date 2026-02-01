package de.unibremen.cs.swp.bokerfi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * DTO für die Zuweisung eines Assets an eine Person.
 *
 * @param personUuid Die UUID der Person, der das Asset zugewiesen werden soll
 */
public record AssetAssignDTO(
        @JsonAlias({"personId", "assigneeId", "userId", "assignee"})
        long personUuid,
        java.time.LocalDateTime returnDeadline
) {}
