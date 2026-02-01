package de.unibremen.cs.swp.bokerfi.controller;

import de.unibremen.cs.swp.bokerfi.dto.LocationCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.LocationDTO;
import de.unibremen.cs.swp.bokerfi.service.LocationCreateService;
import de.unibremen.cs.swp.bokerfi.service.LocationService;
import de.unibremen.cs.swp.bokerfi.service.LocationUpdateService;
import de.unibremen.cs.swp.bokerfi.service.LocationDeleteService;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller für die Verwaltung von Standorten.
 * <p>
 * Bereitstellung von Endpunkten zum Erstellen, Abrufen, Aktualisieren und Löschen von Standorten.
 * Erfordert die Rolle ADMIN oder MANAGER.
 * </p>
 */
@RestController
@RequestMapping(
        value = {"/api/locations", "/locations", "/api/v1/locations"},
        produces = "application/json")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class LocationController {

    private final LocationService service;
    private final LocationCreateService createService;
    private final LocationUpdateService updateService;
    private final LocationDeleteService deleteService;

    /**
     * Erstellt einen neuen LocationController.
     *
     * @param service       Service für allgemeine Standort-Operationen
     * @param createService Service zum Erstellen von Standorten
     * @param updateService Service zum Aktualisieren von Standorten
     * @param deleteService Service zum Löschen von Standorten
     */
    public LocationController(
            LocationService service, 
            LocationCreateService createService,
            LocationUpdateService updateService,
            LocationDeleteService deleteService) {
        this.service = service;
        this.createService = createService;
        this.updateService = updateService;
        this.deleteService = deleteService;
    }

    /**
     * Ruft alle Standorte ab.
     *
     * @return Liste aller LocationDTOs
     */
    @GetMapping
    public List<LocationDTO> findAll() {
        return service.findAll();
    }

    /**
     * Ruft einen Standort anhand seiner ID ab.
     *
     * @param id Die ID des Standorts
     * @return Das LocationDTO
     * @throws BokerfiException wenn der Standort nicht gefunden wird
     */
    @GetMapping("/{id}")
    public LocationDTO findById(@PathVariable long id) {
        return service.findById(id)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Location not found"));
    }

    /**
     * Erstellt einen neuen Standort.
     *
     * @param dto Die Daten für den neuen Standort
     * @return Das erstellte LocationDTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDTO create(@RequestBody LocationCreateDTO dto) {
        return createService.create(dto);
    }
    
    /**
     * Aktualisiert einen bestehenden Standort.
     *
     * @param id  Die ID des zu aktualisierenden Standorts
     * @param dto Die aktualisierten Daten
     * @return Das aktualisierte LocationDTO
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    public LocationDTO update(@PathVariable long id, @RequestBody LocationCreateDTO dto) {
        return updateService.update(id, dto);
    }

    /**
     * Löscht einen Standort anhand seiner ID.
     *
     * @param id Die ID des zu löschenden Standorts
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteService.deleteById(id);
    }
}
