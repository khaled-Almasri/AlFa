package de.unibremen.cs.swp.bokerfi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller für Gesundheitschecks (Health Checks).
 * <p>
 * Bereitstellung eines einfachen Endpunkts, um zu prüfen, ob die Anwendung läuft.
 * </p>
 */
@RestController
public class HealthController {

    /**
     * Führt einen Gesundheitscheck durch.
     * <p>
     * Gibt HTTP 200 OK zurück, wenn die Anwendung läuft.
     * </p>
     */
    @GetMapping({"/health", "/api/health", "/api/v1/health"})
    public void health() {
        // Returns 200 OK by default
    }
}
