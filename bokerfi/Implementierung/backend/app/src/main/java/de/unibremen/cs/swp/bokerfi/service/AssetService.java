package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetDTO;
import de.unibremen.cs.swp.bokerfi.mapper.AssetMapper;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.service.base.BaseService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service für allgemeine Asset-Operationen.
 * <p>
 * Handhabt Suche, Auflistung und Zustandsübergänge wie Übergabe und Wiedereinlagerung.
 * </p>
 */
import de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository; // Add import

@Service
@org.springframework.transaction.annotation.Transactional
public class AssetService extends BaseService<Asset, AssetDTO, AssetRepository> {

    private final AssignmentRepository assignmentRepository;

    /**
     * Erstellt einen neuen AssetService.
     *
     * @param repository Das Asset-Repository
     * @param mapper     Der Asset-Mapper
     * @param assignmentRepository Das Zuweisungs-Repository
     */
    public AssetService(AssetRepository repository, AssetMapper mapper, AssignmentRepository assignmentRepository) {
        super(repository, mapper);
        this.assignmentRepository = assignmentRepository;
    }
    
    /**
     * Findet alle Assets, die einem Mitarbeiter zugewiesen sind (per E-Mail).
     *
     * @param email Die E-Mail-Adresse des Mitarbeiters
     * @return Liste der zugewiesenen AssetDTOs
     */
    public List<AssetDTO> findAllForEmployee(String email) {
        // Use AssignmentRepository to get active assignments with deadlines
        return assignmentRepository.findByPersonEmailAndReturnedAtIsNull(email)
                .stream()
                .map(assignment -> {
                    AssetDTO dto = mapper.toDto(assignment.getAsset());
                    // Re-construct DTO with deadline
                    return new AssetDTO(
                        dto.uuid(), dto.name(), dto.status(), dto.condition(), dto.typeName(),
                        dto.inventoryNumber(), dto.purchaseDate(), dto.warrantyEnd(),
                        dto.assigneeUuid(), dto.locationUuid(), dto.typeUuid(),
                        dto.location(), dto.type(), dto.assignedPerson(), dto.addedAt(),
                        assignment.getReturnDeadline(),
                        assignment.getAssignedAt()
                    );
                })
                .toList();
    }

    /**
     * Sucht nach Assets basierend auf verschiedenen Kriterien.
     *
     * @param assigneeUuid      UUID der zugewiesenen Person
     * @param locationUuid      UUID des Standorts
     * @param typeUuid          UUID des Asset-Typs
     * @param warrantyEndBefore Datum, vor dem die Garantie endet
     * @param sort              Sortierkriterien
     * @return Liste der passenden AssetDTOs
     */


    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<AssetDTO> search(String assigneeUuid, String locationUuid, String typeUuid, LocalDate warrantyEndBefore, Sort sort) {
        try {
            // Fallback: Fetch ALL and filter in memory to avoid JPA Criteria complexity/bugs
            List<Asset> allAssets = findAllAssetsSafely(sort);
            
            return allAssets.stream()
                .filter(asset -> {
                    if (assigneeUuid != null) {
                        return asset.getAssignedPerson() != null && 
                               String.valueOf(asset.getAssignedPerson().getUuid()).equals(assigneeUuid);
                    }
                    return true;
                })
                .filter(asset -> {
                    if (locationUuid != null) {
                        return asset.getLocation() != null && 
                               String.valueOf(asset.getLocation().getUuid()).equals(locationUuid);
                    }
                    return true;
                })
                .filter(asset -> {
                    if (typeUuid != null) {
                        return asset.getType() != null && 
                               String.valueOf(asset.getType().getUuid()).equals(typeUuid);
                    }
                    return true;
                })
                .filter(asset -> {
                    if (warrantyEndBefore != null) {
                        return asset.getWarrantyEnd() != null && 
                               !asset.getWarrantyEnd().isAfter(warrantyEndBefore);
                    }
                    return true;
                })
                .map(mapper::toDto)
                .toList();
            
        } catch (Exception t) {
            throw new de.unibremen.cs.swp.bokerfi.exception.BokerfiException(de.unibremen.cs.swp.bokerfi.exception.ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error during search: " + t.getMessage());
        }
    }

    private List<Asset> findAllAssetsSafely(Sort sort) {
        try {
            return repository.findAll(sort != null ? sort : Sort.unsorted());
        } catch (Exception sortException) {
            // If sorting fails (e.g., invalid field name), fall back to unsorted
            return repository.findAll(Sort.unsorted());
        }
    }
    /**
     * Führt eine Asset-Übergabe aus.
     *
     * @param uuid Die UUID des zu übergebenden Assets
     */
    public void handover(long uuid) {
        Asset asset = repository.findByUuid(uuid)
                .orElseThrow(() -> new de.unibremen.cs.swp.bokerfi.exception.BokerfiException(de.unibremen.cs.swp.bokerfi.exception.ErrorCode.ENTITY_DOES_NOT_EXIST));
        
        // Validation: Can only handover if assigned
        // OR if it is in a state waiting for handover (READY_FOR_INITIAL_PICKUP / READY_FOR_REPAIR_PICKUP)
        // If Status is ASSIGNED, handover might be idempotent or invalid depending on rule.
        // Assuming loose check here or specific transition.
        if (asset.getStatus() == de.unibremen.cs.swp.bokerfi.model.AssetStatus.READY_FOR_INITIAL_PICKUP || 
            asset.getStatus() == de.unibremen.cs.swp.bokerfi.model.AssetStatus.READY_FOR_REPAIR_PICKUP) {
            asset.setStatus(de.unibremen.cs.swp.bokerfi.model.AssetStatus.ASSIGNED);
            
            // Update assignedAt to now (Handover Confirmation Time)
            assignmentRepository.findByAssetAndReturnedAtIsNull(asset).ifPresent(assignment -> {
                assignment.setAssignedAt(java.time.LocalDateTime.now());
                assignmentRepository.save(assignment);
            });
        } else if (asset.getStatus() != de.unibremen.cs.swp.bokerfi.model.AssetStatus.ASSIGNED) {
             // If not assigned and not ready for pickup, error?
             // For now, let's keep it simple as per previous working version.
             throw new de.unibremen.cs.swp.bokerfi.exception.BokerfiException(de.unibremen.cs.swp.bokerfi.exception.ErrorCode.CONFLICT);
        }
        
        repository.save(asset);
        repository.flush();
    }

    /**
     * Lagert ein Asset wieder ein.
     *
     * @param uuid Die UUID des wieder einzulagernden Assets
     */
    public void restock(long uuid) {
        Asset asset = repository.findByUuid(uuid)
                .orElseThrow(() -> new de.unibremen.cs.swp.bokerfi.exception.BokerfiException(de.unibremen.cs.swp.bokerfi.exception.ErrorCode.ENTITY_DOES_NOT_EXIST));
        
        // Prevent restocking if already in stock or other invalid states?
        // Test expects 409.
        if (asset.getStatus() == de.unibremen.cs.swp.bokerfi.model.AssetStatus.IN_STOCK) {
             throw new de.unibremen.cs.swp.bokerfi.exception.BokerfiException(de.unibremen.cs.swp.bokerfi.exception.ErrorCode.CONFLICT);
        }

        asset.setStatus(de.unibremen.cs.swp.bokerfi.model.AssetStatus.IN_STOCK);
        asset.setAssignedPerson(null);
        repository.save(asset);
        repository.flush();
    }
    /**
     * Findet ein Asset anhand seiner UUID.
     * Überschrieben, um Transaktionalität sicherzustellen.
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public java.util.Optional<AssetDTO> findByUuid(long uuid) {
        return super.findByUuid(uuid);
    }

    /**
     * Findet ein Asset anhand seiner ID.
     * Überschrieben, um Transaktionalität sicherzustellen.
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public java.util.Optional<AssetDTO> findById(long id) {
        return super.findById(id);
    }
}
