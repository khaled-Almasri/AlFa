package de.unibremen.cs.swp.bokerfi.controller;

import de.unibremen.cs.swp.bokerfi.dto.ReservationCreateDTO;
import de.unibremen.cs.swp.bokerfi.model.Reservation;
import de.unibremen.cs.swp.bokerfi.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = {"/api/reservations", "/reservations"}, produces = "application/json")
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reservation> getAll(org.springframework.security.core.Authentication authentication) {
        if (authentication == null) return List.of();
        
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
        
        if (isAdminOrManager) {
            return service.findAll();
        } else {
            // Employee: Find by their email/current user
             return service.findAllByPersonEmail(authentication.getName());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> create(@RequestBody ReservationCreateDTO dto) {
        Long uuid = service.create(dto);
        return ResponseEntity.ok(Map.of("uuid", uuid));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable long uuid) {
        service.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
