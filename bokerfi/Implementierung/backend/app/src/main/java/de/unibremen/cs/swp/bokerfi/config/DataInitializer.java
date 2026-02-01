package de.unibremen.cs.swp.bokerfi.config;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import de.unibremen.cs.swp.bokerfi.model.Role;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import de.unibremen.cs.swp.bokerfi.service.PersonCreateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Initialisiert Beispieldaten beim Start der Anwendung.
 * <p>
 * Erstellt Standard-Asset-Typen und Benutzer, falls diese noch nicht existieren.
 * Wird nicht in Test-Umgebungen ausgeführt.
 * </p>
 */
@Component
@Profile("!test") // Nicht in Tests ausführen
public class DataInitializer implements CommandLineRunner {

    private final PersonCreateService personCreateService;
    private final PersonRepository personRepository;
    private final AssetTypeRepository assetTypeRepository;

    public DataInitializer(
            PersonCreateService personCreateService,
            PersonRepository personRepository,
            AssetTypeRepository assetTypeRepository
    ) {
        this.personCreateService = personCreateService;
        this.personRepository = personRepository;
        this.assetTypeRepository = assetTypeRepository;
    }

    @org.springframework.beans.factory.annotation.Value("${bokerfi.admin.password:password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        // Asset-Typen anlegen
        createTypeIfNotExists("Laptop");
        createTypeIfNotExists("Smartphone");
        createTypeIfNotExists("Tablet");
        createTypeIfNotExists("Vehicle");

        // Standard-Benutzer anlegen, falls nicht vorhanden
        createPersonIfNotExists("admin@test.de", "Admin", "User", "A-0001", adminPassword, Role.ADMIN);
        createPersonIfNotExists("manager@test.de", "Manager", "User", "M-0001", adminPassword, Role.MANAGER);
        createPersonIfNotExists("employee@test.de", "Employee", "User", "E-0001", adminPassword, Role.EMPLOYEE);
    }

    private void createTypeIfNotExists(String name) {
        if (assetTypeRepository.findByName(name).isEmpty()) {
            AssetType t = new AssetType();
            t.setName(name);
            assetTypeRepository.save(t);
        }
    }

    private void createPersonIfNotExists(String email, String firstName, String lastName, String pNr, String password, Role role) {
        if (personRepository.findByEmail(email).isEmpty()) {
            personCreateService.create(new PersonCreateDTO(
                    email, firstName, lastName, pNr, password, role,
                    java.util.Collections.emptyList()
            ));
        }
    }
}
