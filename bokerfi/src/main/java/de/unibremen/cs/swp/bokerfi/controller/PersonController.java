package de.unibremen.cs.swp.bokerfi.controller;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.service.PersonCreateService;
import de.unibremen.cs.swp.bokerfi.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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

    public PersonController(PersonService personService,
                            PersonCreateService personCreateService) {
        this.personService = personService;
        this.personCreateService = personCreateService;
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
}
