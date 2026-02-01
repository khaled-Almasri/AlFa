package de.unibremen.cs.swp.bokerfi.controller;

import de.unibremen.cs.swp.bokerfi.dto.*;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller für die Verwaltung von Assets.
 * <p>
 * Bereitstellung von Endpunkten zum Erstellen, Lesen, Aktualisieren, Löschen und Verwalten des Lebenszyklus von Assets.
 * Beinhaltet Funktionen für Asset-Zuweisung, Wartung, Rückgabe und Aussonderung.
 * </p>
 */
@RestController
@RequestMapping(
        value = {"/api/assets", "/assets", "/api/v1/assets"},
        produces = "application/json")
public class AssetController {

    private final AssetService assetService;
    private final AssetCreateService createService;
    private final AssetUpdateService updateService;
    private final AssetDeleteService deleteService;
    private final AssetAssignService assetAssignService;
    private final AssetReturnService assetReturnService;
    private final AssetLostService assetLostService;
    private final AssetMaintenanceStartService maintenanceStartService;
    private final AssetMaintenanceFinishService maintenanceFinishService;
    private final AssetReportDefectService reportDefectService;
    private final AssetRetireService retireService;

    /**
     * Erstellt einen neuen AssetController mit den erforderlichen Services.
     *
     * @param assetService             Service für allgemeine Asset-Operationen
     * @param createService            Service zum Erstellen von Assets
     * @param updateService            Service zum Aktualisieren von Assets
     * @param deleteService            Service zum Löschen von Assets
     * @param assetAssignService       Service für Asset-Zuweisungen
     * @param assetReturnService       Service für Asset-Rückgaben
     * @param assetLostService         Service für verlorene Assets
     * @param maintenanceStartService  Service zum Starten von Wartungen
     * @param maintenanceFinishService Service zum Beenden von Wartungen
     * @param reportDefectService      Service für Defektmeldungen
     * @param retireService            Service für Aussonderung
     */
    @SuppressWarnings("java:S107")
    public AssetController(
            AssetService assetService,
            AssetCreateService createService,
            AssetUpdateService updateService,
            AssetDeleteService deleteService,
            AssetAssignService assetAssignService,
            AssetReturnService assetReturnService,
            AssetLostService assetLostService,
            AssetMaintenanceStartService maintenanceStartService,
            AssetMaintenanceFinishService maintenanceFinishService,
            AssetReportDefectService reportDefectService,
            AssetRetireService retireService
    ) {
        this.assetService = assetService;
        this.createService = createService;
        this.updateService = updateService;
        this.deleteService = deleteService;
        this.assetAssignService = assetAssignService;
        this.assetReturnService = assetReturnService;
        this.assetLostService = assetLostService;
        this.maintenanceStartService = maintenanceStartService;
        this.maintenanceFinishService = maintenanceFinishService;
        this.reportDefectService = reportDefectService;
        this.retireService = retireService;
    }

    /**
     * Ruft eine Liste aller Assets ab, optional gefiltert und sortiert.
     *
     * @param authentication  Die Authentifizierung des aktuellen Benutzers
     * @param assigneeId      Filter nach Zuweisungs-ID
     * @param assignee        Alias für assigneeId
     * @param assigneeIdSnake Alias für assigneeId (snake_case)
     * @param locationId      Filter nach Standort-ID
     * @param location        Alias für locationId
     * @param locationIdSnake Alias für locationId (snake_case)
     * @param typeId          Filter nach Asset-Typ-ID
     * @param type            Alias für typeId
     * @param typeIdSnake     Alias für typeId (snake_case)
     * @param warrantyEndBefore Filter für Garantieende vor diesem Datum
     * @param sortBy          Feld zum Sortieren
     * @param sortByCap       Alias für sortBy (Großgeschrieben)
     * @param order           Sortierreihenfolge (ASC oder DESC)
     * @param sortDirection   Alias für order
     * @return Liste der passenden AssetDTOs
     */
    @GetMapping
    public List<AssetDTO> findAll(
            org.springframework.security.core.Authentication authentication,
            @RequestParam(required = false) String assigneeId,
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false, name = "assignee_id") String assigneeIdSnake,
            @RequestParam(required = false) String locationId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, name = "location_id") String locationIdSnake,
            @RequestParam(required = false) String typeId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, name = "type_id") String typeIdSnake,
            @RequestParam(required = false) java.time.LocalDate warrantyEndBefore,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, name = "SortBy") String sortByCap,
            @RequestParam(required = false) String order,
            @RequestParam(required = false, name = "sortDirection") String sortDirection
    ) {

        boolean isEmployee = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));
        
        if (isEmployee) {
            return assetService.findAllForEmployee(authentication.getName());
        }
        
        // Resolve aliases
        String finalAssigneeId = resolveAlias(assigneeId, assignee, assigneeIdSnake);
        String finalLocationId = resolveAlias(locationId, location, locationIdSnake);
        String finalTypeId = resolveAlias(typeId, type, typeIdSnake);

        org.springframework.data.domain.Sort sort = resolveSort(sortBy, sortByCap, order, sortDirection);

        return assetService.search(finalAssigneeId, finalLocationId, finalTypeId, warrantyEndBefore, sort);
    }

    private String resolveAlias(String val, String alias1, String alias2) {
        if (val != null) {
            return val;
        } else if (alias1 != null) {
            return alias1;
        } else {
            return alias2;
        }
    }

    private org.springframework.data.domain.Sort resolveSort(String sortBy, String sortByCap, String order, String sortDirection) {
        if (sortBy == null && sortByCap == null) {
            return org.springframework.data.domain.Sort.unsorted();
        }

        String targetSortBy = sortBy != null ? sortBy : sortByCap;
        String targetOrder = order != null ? order : sortDirection;

        // Map field names to valid JPA entity fields
        String mappedField = mapSortField(targetSortBy);

        org.springframework.data.domain.Sort.Direction direction = org.springframework.data.domain.Sort.Direction.ASC;
        if (targetOrder != null && targetOrder.equalsIgnoreCase("desc")) {
            direction = org.springframework.data.domain.Sort.Direction.DESC;
        }
        return org.springframework.data.domain.Sort.by(direction, mappedField);
    }
    
    private String mapSortField(String field) {
        if (field == null) return "uuid";
        
        // Handle snake_case to camelCase conversion
        String normalized = field.replace("_", "");
        
        // Map common field variations
        return switch (normalized.toLowerCase()) {
            case "assigneeid", "assignee", "assignedperson", "assignedpersonid" -> "assignedPerson";
            case "locationid", "location" -> "location";
            case "typeid", "type", "assettype" -> "type";
            case "inventorynumber", "inventory" -> "inventoryNumber";
            case "purchasedate", "purchase" -> "purchaseDate";
            case "warrantyend", "warranty" -> "warrantyEnd";
            case "condition" -> "condition";
            case "status" -> "status";
            case "name" -> "name";
            case "uuid", "id" -> "uuid";
            default -> "uuid"; // Fallback to safe default
        };
    }

    private long parseUuid(String uuidStr) {
        try {
            return Long.parseLong(uuidStr.trim());
        } catch (NumberFormatException e) {
            throw new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Invalid UUID format: " + uuidStr);
        }
    }

    /**
     * Erstellt ein neues Asset.
     *
     * @param dto Die Daten für das neue Asset
     * @return Das erstellte AssetDTO
     */
    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public AssetDTO create(@RequestBody AssetCreateDTO dto) {
        return createService.create(dto);
    }

    /**
     * Aktualisiert ein bestehendes Asset.
     *
     * @param uuidStr Die UUID des zu aktualisierenden Assets
     * @param dto  Die aktualisierten Daten für das Asset
     * @return Das aktualisierte AssetDTO
     */
    @RequestMapping(value = "/{uuid}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public AssetDTO update(@PathVariable("uuid") String uuidStr, @RequestBody AssetCreateDTO dto) {
        long uuid = parseUuid(uuidStr);
        Long updatedUuid = updateService.update(uuid, dto);
        return assetService.findByUuid(updatedUuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Asset not found"));
    }

    /**
     * Löscht ein Asset anhand seiner UUID.
     *
     * @param uuidStr Die UUID des zu löschenden Assets
     */
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void delete(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        deleteService.delete(uuid);
    }

    /**
     * Meldet einen Defekt für ein Asset.
     */
    @PostMapping(value = "/{uuid}/defect")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public void reportDefect(@PathVariable("uuid") String uuidStr, @RequestBody java.util.Map<String, Object> body) {
        long uuid = parseUuid(uuidStr);
        String description = (String) body.get("description");
        if (description == null || description.trim().isEmpty()) {
            throw new BokerfiException(ErrorCode.CONSTRAINT_VIOLATION, "Description missing");
        }
        
        // Resolver user from body or context. Simplified:
        Long reporterUuid = null;
        if (body.containsKey("reporterUuid")) {
            reporterUuid = ((Number) body.get("reporterUuid")).longValue();
        } else {
             // ...
        }
        
        if (reporterUuid == null) {
             throw new BokerfiException(ErrorCode.CONSTRAINT_VIOLATION, "reporterUuid missing");
        }
        
        reportDefectService.reportDefect(uuid, reporterUuid, description);
    }

    /**
     * Weist ein Asset einer Person zu.
     *
     * @param uuidStr Die UUID des zuzuweisenden Assets
     * @param dto  Die Zuweisungsdetails inklusive der Personen-UUID
     * @return Eine Map mit der UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/assign", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH}, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public java.util.Map<String, Object> assign(
            @PathVariable("uuid") String uuidStr,
            @RequestBody AssetAssignDTO dto
    ) {
        long uuid = parseUuid(uuidStr);
        long assetUuid = assetAssignService.assign(uuid, dto.personUuid(), dto.returnDeadline());
        return java.util.Collections.singletonMap("uuid", String.valueOf(assetUuid));
    }

    /**
     * Verlängert die Rückgabefrist eines Assets.
     *
     * @param uuidStr Die UUID des Assets
     * @param body Map mit "deadline" als Key
     */
    @RequestMapping(value = "/{uuid}/extend-deadline", method = {RequestMethod.POST, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void extendDeadline(@PathVariable("uuid") String uuidStr, @RequestBody java.util.Map<String, String> body) {
        long uuid = parseUuid(uuidStr);
        java.time.LocalDateTime deadline = java.time.LocalDateTime.parse(body.get("deadline"));
        assetAssignService.extendDeadline(uuid, deadline);
    }

    /**
     * Markiert ein Asset für die endgültige Rückgabe (INITIATED).
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/mark-for-final-return", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public void markForFinalReturn(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        assetReturnService.markForFinalReturn(uuid);
    }

    /**
     * Bestätigt eine endgültige Rückgabe (COMPLETED).
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/confirm-final-return", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void confirmFinalReturn(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        assetReturnService.confirmFinalReturn(uuid);
    }

    /**
     * Direktes Zurückgeben (Shortcut).
     */
    @RequestMapping(value = "/{uuid}/return", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void returnAsset(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        assetReturnService.returnAsset(uuid);
    }

    /**
     * Markiert ein Asset als verloren.
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/mark-lost", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public void markLost(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        assetLostService.markLost(uuid);
    }

    /**
     * Bestätigt, dass ein Asset verloren ist.
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/confirm-lost", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void confirmLost(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        assetLostService.confirmLoss(uuid);
    }

    /**
     * Startet die Wartung (Reparatur oder final) für ein Asset.
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = {"/{uuid}/maintenance/start", "/{uuid}/begin-repair-maintenance", "/{uuid}/begin-final-maintenance"}, method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void startMaintenance(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        maintenanceStartService.startMaintenance(uuid);
    }

    /**
     * Beendet die Wartung für ein Asset.
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/maintenance/finish", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void finishMaintenance(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        maintenanceFinishService.finishMaintenance(uuid);
    }

    /**
     * Markiert ein Asset für die Reparatur-Rückgabe (User Action).
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/mark-for-repair-return", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public void markForRepairReturn(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        assetReturnService.markForRepairReturn(uuid);
    }

    /**
     * Beendet eine Reparatur oder bestätigt die Rückgabe aus der Reparatur.
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/confirm-repair-return", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void confirmRepairReturn(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        assetReturnService.confirmRepairReturn(uuid);
    }

    /**
     * Beendet eine Reparatur.
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/finish-repair", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void finishRepair(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        maintenanceFinishService.finishRepair(uuid);
    }

    /**
     * Sondert ein Asset aus.
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = "/{uuid}/retire", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void retire(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        retireService.retire(uuid);
    }

    /**
     * Lagert ein Asset wieder ein.
     *
     * @param uuidStr Die UUID des Assets
     */
    @RequestMapping(value = {"/{uuid}/restock", "/{uuid}/confirm-restock", "/{uuid}/return-to-stock"}, method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void restock(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        assetService.restock(uuid);
    }

    /**
     * Ruft ein Asset anhand seiner UUID ab.
     *
     * @param uuidStr Die UUID des Assets
     * @return Das AssetDTO
     * @throws BokerfiException wenn das Asset nicht gefunden wird
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public AssetDTO findById(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        return assetService.findByUuid(uuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Asset not found"));
    }

    /**
     * Führt eine Asset-Übergabe oder Bestätigung durch.
     *
     * @param uuid Die UUID des Assets
     */
    @PostMapping(value = {"/{uuid}/handover", "/{uuid}/confirm-handover", "/{uuid}/accept-handover"})
    @PatchMapping(value = {"/{uuid}/handover", "/{uuid}/confirm-handover", "/{uuid}/accept-handover"})
    @PutMapping(value = {"/{uuid}/handover", "/{uuid}/confirm-handover", "/{uuid}/accept-handover"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void handover(@PathVariable("uuid") String uuidStr) {
        long uuid = parseUuid(uuidStr);
        assetService.handover(uuid);
    }

}
