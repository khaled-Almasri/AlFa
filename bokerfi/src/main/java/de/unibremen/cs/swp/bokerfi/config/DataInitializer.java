package de.unibremen.cs.swp.bokerfi.config;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.service.PersonCreateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import de.unibremen.cs.swp.bokerfi.service.FacilityCreateService;
import de.unibremen.cs.swp.bokerfi.dto.FacilityCreateDTO;

/**
 * Initialisiert Beispieldaten beim Start der Anwendung.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final PersonCreateService personCreateService;
    private final FacilityCreateService facilityCreateService;

    /**
     * Konstruktor für die Initialisierung der benötigten Services.
     * Die Services werden von Spring automatisch injiziert
     * (Constructor Injection).
     *
     * @param personCreateService   Service zum Erstellen von Personen
     * @param facilityCreateService Service zum Erstellen von Räumen
     */
    public DataInitializer(PersonCreateService personCreateService, FacilityCreateService facilityCreateService) {
        this.personCreateService = personCreateService;
        this.facilityCreateService = facilityCreateService;
    }

    /**
     * Wird automatisch beim Start der Anwendung ausgeführt.
     * Hier werden Beispielpersonen und ein Beispielraum
     * im Datenbestand angelegt.
     *
     * @param args Startparameter der Anwendung (werden hier nicht verwendet)
     */
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

        FacilityCreateDTO senatssaal =
                new FacilityCreateDTO("MZH 1380/1400");

        facilityCreateService.createFacility(senatssaal);

    }
}
