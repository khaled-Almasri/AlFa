package de.unibremen.cs.swp.bokerfi.dto;

import jakarta.validation.constraints.NotBlank;

public record FacilityCreateDTO(
        @NotBlank
        String name
) {
}
