package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetCreateDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import de.unibremen.cs.swp.bokerfi.model.Location;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import org.springframework.stereotype.Service;

/**
 * Service zum Aktualisieren bestehender Assets.
 * <p>
 * Handhabt Aktualisierungen von Asset-Eigenschaften einschließlich Beziehungen (Typ, Standort).
 * </p>
 */
@Service
@org.springframework.transaction.annotation.Transactional
public class AssetUpdateService {

    private final AssetRepository repository;
    private final AssetTypeRepository typeRepository;
    private final LocationRepository locationRepository;

    /**
     * Erstellt einen neuen AssetUpdateService.
     *
     * @param repository         Das Asset-Repository
     * @param typeRepository     Das Asset-Typ-Repository
     * @param locationRepository Das Standort-Repository
     */
    public AssetUpdateService(
            AssetRepository repository,
            AssetTypeRepository typeRepository,
            LocationRepository locationRepository
    ) {
        this.repository = repository;
        this.typeRepository = typeRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Aktualisiert ein bestehendes Asset.
     *
     * @param uuid The UUID des zu aktualisierenden Assets
     * @param dto  Das DTO mit den aktualisierten Daten
     * @return Die UUID des aktualisierten Assets
     */
    /**
     * Aktualisiert ein bestehendes Asset.
     *
     * @param uuid The UUID des zu aktualisierenden Assets
     * @param dto  Das DTO mit den aktualisierten Daten
     * @return Die UUID des aktualisierten Assets
     */
    @SuppressWarnings("null")
    public Long update(long uuid, AssetCreateDTO dto) {
        try {
            Asset asset = repository.findByUuid(uuid)
                    .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Asset not found"));

            updateBasicFields(asset, dto);
            updateLocation(asset, dto);
            updateType(asset, dto);
            
            repository.saveAndFlush(asset);
            return asset.getUuid();
            
        } catch (BokerfiException e) {
            throw e;
        } catch (Exception e) {
            // Unchecked exception during update
            throw new BokerfiException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error during update: " + e.getMessage());
        }
    }

    private void updateBasicFields(Asset asset, AssetCreateDTO dto) {
        if (dto.inventoryNumber() != null) asset.setInventoryNumber(dto.inventoryNumber());
        if (dto.name() != null) asset.setName(dto.name());
        if (dto.purchaseDate() != null) asset.setPurchaseDate(dto.purchaseDate());
        if (dto.warrantyEnd() != null) asset.setWarrantyEnd(dto.warrantyEnd());
        if (dto.condition() != null) asset.setCondition(dto.condition());
    }

    private void updateLocation(Asset asset, AssetCreateDTO dto) {
        if (dto.locationUuid() != null) {
            String locationUuidStr = String.valueOf(dto.locationUuid());
            try {
                if (!locationUuidStr.equals("null") && !locationUuidStr.isEmpty()) {
                    long lUuid = Long.parseLong(locationUuidStr);
                    Location location = locationRepository.findByUuid(lUuid)
                            .or(() -> locationRepository.findById(lUuid))
                            .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Location not found"));
                    asset.setLocation(location);
                }
            } catch (NumberFormatException e) {
                throw new BokerfiException(ErrorCode.CONSTRAINT_VIOLATION, "Invalid location UUID: " + locationUuidStr);
            }
        }
    }

    private void updateType(Asset asset, AssetCreateDTO dto) {
        if (dto.typeUuid() != null) {
            String typeUuidStr = String.valueOf(dto.typeUuid());
            try {
                if (!typeUuidStr.equals("null") && !typeUuidStr.isEmpty()) {
                    long tUuid = Long.parseLong(typeUuidStr);
                    AssetType type = typeRepository.findByUuid(tUuid)
                            .or(() -> typeRepository.findById(tUuid))
                            .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Type not found"));
                    asset.setType(type);
                }
            } catch (NumberFormatException e) {
                throw new BokerfiException(ErrorCode.CONSTRAINT_VIOLATION, "Invalid type UUID: " + typeUuidStr);
            }
        }
    }
}
