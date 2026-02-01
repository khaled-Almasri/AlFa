package de.unibremen.cs.swp.bokerfi.controller;

import de.unibremen.cs.swp.bokerfi.dto.*;
import de.unibremen.cs.swp.bokerfi.service.*;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller für die Verwaltung von Asset-Typen.
 * <p>
 * Bereitstellung von Endpunkten zum Erstellen, Abrufen, Aktualisieren und Löschen von Asset-Typen.
 * Erfordert die Rolle ADMIN oder MANAGER für alle Operationen.
 * </p>
 */
@RestController
@RequestMapping(
        value = {"/api/assetTypes", "/api/asset-types", "/assets/types", "/api/v1/asset-types", "/api/v1/assets/types"},
        produces = "application/json"
)
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class AssetTypeController {

    private final AssetTypeService service;
    private final AssetTypeCreateService createService;
    private final AssetTypeUpdateService updateService;
    private final AssetTypeDeleteService deleteService;

    /**
     * Erstellt einen neuen AssetTypeController.
     *
     * @param service       Service für allgemeine Asset-Typ-Operationen
     * @param createService Service zum Erstellen von Asset-Typen
     * @param updateService Service zum Aktualisieren von Asset-Typen
     * @param deleteService Service zum Löschen von Asset-Typen
     */
    public AssetTypeController(
            AssetTypeService service,
            AssetTypeCreateService createService,
            AssetTypeUpdateService updateService,
            AssetTypeDeleteService deleteService
    ) {
        this.service = service;
        this.createService = createService;
        this.updateService = updateService;
        this.deleteService = deleteService;
    }

    /**
     * Ruft alle Asset-Typen ab.
     *
     * @return Liste aller AssetTypeDTOs
     */
    @GetMapping
    public List<AssetTypeDTO> findAll() {
        return service.findAll();
    }
    
    /**
     * Ruft einen Asset-Typ anhand seiner ID ab.
     *
     * @param id Die ID des Asset-Typs
     * @return Das AssetTypeDTO
     * @throws BokerfiException wenn der Asset-Typ nicht gefunden wird
     */
    @GetMapping("/{id}")
    public AssetTypeDTO findById(@PathVariable long id) {
        return service.findById(id)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "AssetType not found"));
    }

    /**
     * Erstellt einen neuen Asset-Typ.
     *
     * @param dto Die Daten für den neuen Asset-Typ
     * @return Das erstellte AssetTypeDTO
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public AssetTypeDTO create(@RequestBody AssetTypeCreateDTO dto) {
        return createService.create(dto);
    }
    
    /**
     * Aktualisiert einen bestehenden Asset-Typ.
     *
     * @param id Die ID des zu aktualisierenden Asset-Typs
     * @param dto  Die aktualisierten Daten
     * @return Das aktualisierte AssetTypeDTO
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public AssetTypeDTO update(@PathVariable long id, @RequestBody AssetTypeCreateDTO dto) {
        return updateService.update(id, dto);
    }
    
    /**
     * Löscht einen Asset-Typ anhand seiner ID.
     *
     * @param id Die ID des zu löschenden Asset-Typs
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        deleteService.deleteById(id);
    }
}
