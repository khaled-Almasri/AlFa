package de.unibremen.cs.swp.bokerfi.config;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.service.PersonCreateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialisiert Beispieldaten beim Start der Anwendung.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final PersonCreateService personCreateService;

    public DataInitializer(PersonCreateService personCreateService) {
        this.personCreateService = personCreateService;
    }

    @Override
    public void run(String... args) {

        personCreateService.createPerson(
                new PersonCreateDTO(
                        "Max",
                        "Mustermann",
                        "max@test.de"
                )
        );

        personCreateService.createPerson(
                new PersonCreateDTO(
                        "Erika",
                        "Musterfrau",
                        "erika@test.de"
                )
        );
    }
}
