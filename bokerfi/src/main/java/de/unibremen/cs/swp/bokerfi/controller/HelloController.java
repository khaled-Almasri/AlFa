package de.unibremen.cs.swp.bokerfi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * REST-Controller, der einen einfachen "Hello World"-Endpunkt bereitstellt.
 * <p>
 * Diese Klasse stellt einen HTTP-GET-Endpunkt unter {@code /api/hello} zur Verfügung,
 * der als JSON eine Begrüßungsnachricht zurückliefert.
 * </p>
 * @author K. Hölscher
 * @version 2025-10-01
 */
@RestController
public class HelloController {

    /**
     * Logger für serverseitige Protokollierung.
     * <p>
     * Der Logger wird über SLF4J bereitgestellt und dient zur Nachvollziehbarkeit
     * von Methodenaufrufen und internen Abläufen.
     * </p>
     */
    private static final Logger log =
            LoggerFactory.getLogger(HelloController.class);
    /**
     * Standardkonstruktor.
     * <p>
     * Wird explizit definiert, um die Klasse sauber zu dokumentieren
     * und spätere Erweiterungen zu ermöglichen.
     * </p>
     */
    public HelloController() {
        // Standardkonstruktor, explizit für Javadoc-Generierung
    }

    /**
     * Behandelt GET-Anfragen für {@code /api/hello}.
     * <p>
     * Gibt eine einfache Begrüßungsnachricht als JSON-Objekt zurück.
     * Zusätzlich wird der Aufruf der Methode serverseitig geloggt,
     * um das Logging-Konzept zu demonstrieren.
     * </p>
     *
     * @return Eine Map mit dem Schlüssel {@code message} und dem Wert {@code Hello SWP!}.
     */
    @GetMapping("/api/hello")
    public Map<String, String> hello() {
        log.info("Method hello has been called.");
        return Map.of("message", "Hello SWP!");
    }
}
