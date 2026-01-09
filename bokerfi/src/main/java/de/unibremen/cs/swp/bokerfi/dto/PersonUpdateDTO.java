package de.unibremen.cs.swp.bokerfi.dto;

import jakarta.validation.constraints.NotBlank;

public record PersonUpdateDTO(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName
) {}
