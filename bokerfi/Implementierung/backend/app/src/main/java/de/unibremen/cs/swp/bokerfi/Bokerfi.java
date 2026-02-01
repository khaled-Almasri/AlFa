package de.unibremen.cs.swp.bokerfi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Die Haupteinstiegspunkt-Klasse für die Spring Boot-Anwendung Bokerfi.
 * <p>
 * Diese Klasse wird genutzt, um die Anwendung zu starten. Sie initialisiert
 * den Spring Application Context, konfiguriert die Komponenten und startet
 * den eingebetteten Webserver.
 * </p>
 * @author K. Hölscher
 * @version 2025-10-01
 */
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Die Haupteinstiegspunkt-Klasse für die Spring Boot-Anwendung Bokerfi.
 * <p>
 * Diese Klasse wird genutzt, um die Anwendung zu starten. Sie initialisiert
 * den Spring Application Context, konfiguriert die Komponenten und startet
 * den eingebetteten Webserver.
 * </p>
 * @author K. Hölscher
 * @version 2025-10-01
 */
@SpringBootApplication(scanBasePackages = "de.unibremen.cs.swp.bokerfi")
@EnableScheduling
public class Bokerfi {

    /**
     * Erzeugt eine neue Instanz von {@code Bokerfi}.
     */
    public Bokerfi() {
        // Standardkonstruktor, explizit für Javadoc-Generierung
    }

    /**
     * Startet die Spring Boot-Anwendung.
     * <p>
     * Diese Methode bootet den eingebetteten Application-Server
     * (z. B. Tomcat) und lädt den Application Context von Spring.
     * </p>
     *
     * @param args Kommandozeilenargumente, die beim Starten an die Anwendung übergeben werden können.
     */
    public static void main(String[] args) {
        SpringApplication.run(Bokerfi.class, args);
    }

    
}
