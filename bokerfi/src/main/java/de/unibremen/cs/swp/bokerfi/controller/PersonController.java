package de.unibremen.cs.swp.bokerfi.controller;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonUpdateDTO;
import de.unibremen.cs.swp.bokerfi.service.PersonCreateService;
import de.unibremen.cs.swp.bokerfi.service.PersonService;
import de.unibremen.cs.swp.bokerfi.service.PersonUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import de.unibremen.cs.swp.bokerfi.service.PersonDeleteService;

import java.net.URI;
import java.util.List;

/**
 * REST-Controller für Personen.
 */
@RestController
public class PersonController {

    private static final Logger log =
            LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;
    private final PersonCreateService personCreateService;
    private final PersonUpdateService personUpdateService;
    private final PersonDeleteService personDeleteService;

    public PersonController(
            PersonService personService,
            PersonCreateService personCreateService,
            PersonUpdateService personUpdateService,
            PersonDeleteService personDeleteService
    ) {
        this.personService = personService;
        this.personCreateService = personCreateService;
        this.personUpdateService = personUpdateService;
        this.personDeleteService = personDeleteService;
    }
    /**
     * Liefert alle Personen.
     */
    @GetMapping("/api/persons")
    public List<PersonDTO> getPersons() {
        log.info("GET /api/persons called");
        return personService.findAll();
    }

    /**
     * Erstellt eine neue Person.
     */
    @PostMapping("/api/persons")
    public ResponseEntity<PersonDTO> createPerson(
            @Valid @RequestBody PersonCreateDTO dto) {

        PersonDTO saved =
                personCreateService.createPerson(dto);

        return ResponseEntity
                .created(URI.create("/api/persons/" + saved.uuid()))
                .body(saved);
    }
    /**
     * Aktualisiert teilweise (PATCH) eine Person.
     */
    @PatchMapping("/api/persons/{uuid}")
    public ResponseEntity<PersonDTO> updatePerson(
            @PathVariable Long uuid,
            @Valid @RequestBody PersonUpdateDTO dto
    ) {
        PersonDTO updated = personUpdateService.applyUpdate(dto, uuid);
        return ResponseEntity.ok(updated);
    }
    /**
     * Löscht eine Person.
     * Gibt 204 (NO CONTENT) zurück, wenn das Löschen erfolgreich war.
     */
    @DeleteMapping("/api/persons/{uuid}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long uuid) {
        personDeleteService.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }

}
