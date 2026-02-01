package de.unibremen.cs.swp.bokerfi.controller;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonUpdateDTO;
import de.unibremen.cs.swp.bokerfi.service.PersonCreateService;
import de.unibremen.cs.swp.bokerfi.service.PersonService;
import de.unibremen.cs.swp.bokerfi.service.PersonUpdateService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import de.unibremen.cs.swp.bokerfi.service.PersonDeleteService;

import org.springframework.security.access.prepost.PreAuthorize;
import java.net.URI;
import java.util.List;

/**
 * Controller für die Verwaltung von Personen (Benutzern).
 * <p>
 * Bereitstellung von Endpunkten zum Erstellen, Abrufen, Aktualisieren und Löschen von Benutzern,
 * sowie zum Verwalten von Benutzerstatus und Passwörtern.
 * </p>
 */
@RestController
// Permission handled per method
public class PersonController {



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
     * Ruft alle Personen ab.
     *
     * @return Liste aller PersonDTOs
     */
    @GetMapping({"/api/persons", "/persons", "/api/v1/persons", "/api/users", "/users", "/api/v1/users"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonDTO>> getPersons() {
        List<PersonDTO> persons = personService.findAll();

        // Simple ETag using "0" as start value since DTO has no version
        return ResponseEntity
                .ok()
                .eTag("\"0\"")
                .body(persons);
    }

    /**
     * Ruft eine Liste von Personen für Zuweisungszwecke ab.
     *
     * @return Liste aller PersonDTOs
     */
    @GetMapping({"/api/persons/assignees", "/api/v1/persons/assignees"})
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<PersonDTO>> getAssignees() {
        return ResponseEntity.ok(personService.findAll());
    }


    /**
     * Erstellt eine neue Person.
     *
     * @param dto Die Daten für die neue Person
     * @return Das erstellte PersonDTO
     */
    @PostMapping({"/api/persons", "/persons", "/api/v1/persons", "/api/users", "/users", "/api/v1/users"})
    @PreAuthorize("hasRole('ADMIN')")
    @SuppressWarnings("null")
    public ResponseEntity<PersonDTO> createPerson(
            @Valid @RequestBody PersonCreateDTO dto) {

        PersonDTO saved =
                personCreateService.create(dto);

        return ResponseEntity
                .created(URI.create("/api/persons/" + saved.uuid()))
                .body(saved);
    }
    /**
     * Aktualisiert eine Person.
     *
     * @param uuid    Die UUID der zu aktualisierenden Person
     * @param ifMatch Der If-Match Header für optimistisches Locking
     * @param dto     Die aktualisierten Daten
     * @return Das aktualisierte PersonDTO
     */
    @RequestMapping(value = {"/api/persons/{uuid}", "/persons/{uuid}", "/api/v1/persons/{uuid}", "/api/users/{uuid}", "/users/{uuid}", "/api/v1/users/{uuid}"},
            method = {RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonDTO> updatePerson(
            @PathVariable Long uuid,
            @RequestHeader(value = "If-Match", required = false) String ifMatch,
            @Valid @RequestBody PersonUpdateDTO dto
    ) {
        long version = 0L;
        if (ifMatch != null) {
            try {
                version = Long.parseLong(ifMatch.replace("\"", ""));
            } catch (NumberFormatException e) {
                // Ignore invalid header
            }
        }
        
        PersonDTO updated = personUpdateService.applyUpdate(dto, uuid, version);
        return ResponseEntity.ok(updated);
    }
    /**
     * Löscht eine Person.
     *
     * @param uuid Die UUID der zu löschenden Person
     * @return No Content bei Erfolg
     */
    @DeleteMapping({"/api/persons/{uuid}", "/persons/{uuid}", "/api/v1/persons/{uuid}", "/api/users/{uuid}", "/users/{uuid}", "/api/v1/users/{uuid}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePerson(@PathVariable Long uuid) {
        personDeleteService.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * Ruft eine Person anhand der UUID ab.
     *
     * @param uuid Die UUID der Person
     * @return Das PersonDTO
     */
    @GetMapping({"/api/persons/{uuid}", "/persons/{uuid}", "/api/v1/persons/{uuid}", "/api/users/{uuid}", "/users/{uuid}", "/api/v1/users/{uuid}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long uuid) {
        return personService.findByUuid(uuid)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new de.unibremen.cs.swp.bokerfi.exception.BokerfiException(de.unibremen.cs.swp.bokerfi.exception.ErrorCode.ENTITY_DOES_NOT_EXIST));
    }

    /**
     * Ändert das Passwort eines Benutzers (Admin-Funktion).
     *
     * @param uuid Die UUID des Benutzers
     * @param body Eine Map mit dem neuen "password"
     * @return No Content bei Erfolg
     */
    @RequestMapping(value = {"/api/persons/{uuid}/password", "/persons/{uuid}/password", "/api/v1/persons/{uuid}/password", "/api/users/{uuid}/password", "/users/{uuid}/password", "/api/v1/users/{uuid}/password"},
            method = {RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changePassword(@PathVariable Long uuid, @RequestBody java.util.Map<String, String> body) {
        String password = body.getOrDefault("password", body.get("newPassword"));
        if (password == null) {
            return ResponseEntity.badRequest().build();
        }
        personUpdateService.updatePassword(uuid, password);
        return ResponseEntity.noContent().build();
    }

    /**
     * Aktiviert ein Benutzerkonto.
     *
     * @param uuid Die UUID des Benutzers
     * @return No Content bei Erfolg
     */
    @RequestMapping(value = {"/api/persons/{uuid}/activate", "/persons/{uuid}/activate", "/api/v1/persons/{uuid}/activate", "/api/users/{uuid}/activate", "/users/{uuid}/activate", "/api/v1/users/{uuid}/activate"},
            method = {RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateUser(@PathVariable Long uuid) {
        personUpdateService.updateStatus(uuid, true);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Deaktiviert ein Benutzerkonto.
     *
     * @param uuid Die UUID des Benutzers
     * @return No Content bei Erfolg
     */
    @RequestMapping(value = {"/api/persons/{uuid}/deactivate", "/persons/{uuid}/deactivate", "/api/v1/persons/{uuid}/deactivate", "/api/users/{uuid}/deactivate", "/users/{uuid}/deactivate", "/api/v1/users/{uuid}/deactivate"},
            method = {RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long uuid) {
        personUpdateService.updateStatus(uuid, false);
        return ResponseEntity.noContent().build();
    }


}
