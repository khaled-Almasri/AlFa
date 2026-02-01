package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.AssetDTO;
import de.unibremen.cs.swp.bokerfi.exception.*;
import de.unibremen.cs.swp.bokerfi.mapper.AssetMapper;
import de.unibremen.cs.swp.bokerfi.model.*;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.*;
import org.springframework.stereotype.Service;

/**
 * Service zum Erstellen neuer Assets.
 * <p>
 * Handhabt die Auflösung von Abhängigkeiten (Typ, Standort) und die Erstellung der Asset-Entität.
 * </p>
 */
@Service
@org.springframework.transaction.annotation.Transactional
@lombok.extern.slf4j.Slf4j
public class AssetCreateService {

    private final AssetRepository assetRepository;
    private final AssetTypeRepository typeRepository;
    private final LocationRepository locationRepository;
    private final AssetMapper assetMapper;
    private final HistoryService historyService;

    /**
     * Erstellt einen neuen AssetCreateService.
     *
     * @param assetRepository    Das Asset-Repository
     * @param typeRepository     Das Asset-Typ-Repository
     * @param locationRepository Das Standort-Repository
     * @param assetMapper        Der Asset-Mapper
     */
    public AssetCreateService(
            AssetRepository assetRepository,
            AssetTypeRepository typeRepository,
            LocationRepository locationRepository,
            AssetMapper assetMapper,
            HistoryService historyService
    ) {
        this.assetRepository = assetRepository;
        this.typeRepository = typeRepository;
        this.locationRepository = locationRepository;
        this.assetMapper = assetMapper;
        this.historyService = historyService;
    }

    /**
     * Erstellt ein neues Asset aus dem gegebenen DTO.
     *
     * @param dto Das DTO mit den Asset-Daten
     * @return Das erstellte AssetDTO
     */
    public AssetDTO create(AssetCreateDTO dto) {
        // 1. Resolve AssetType
        String typeUuidStr = dto.typeUuid(); 
        long tUuid;
        try {
            if (typeUuidStr == null) throw new NumberFormatException("null");
            tUuid = Long.parseLong(typeUuidStr);
        } catch (NumberFormatException e) {
            throw new BokerfiException(ErrorCode.CONSTRAINT_VIOLATION, "Invalid type UUID: " + typeUuidStr);
        }
        
        log.debug("Looking for AssetType UUID: {}", tUuid);
        AssetType type = typeRepository.findByUuid(tUuid)
                .or(() -> typeRepository.findById(tUuid))
                .orElseThrow(() -> {
                    log.error("AssetType NOT FOUND for UUID: {}", tUuid);
                    return new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "AssetType not found for UUID: " + tUuid);
                });

        // 2. Resolve Location
        String locationUuidRaw = dto.locationUuid();
        
        long locationUuid;
        try {
            if (locationUuidRaw == null) throw new NumberFormatException("null");
            locationUuid = Long.parseLong(locationUuidRaw);
        } catch (NumberFormatException e) {
             throw new BokerfiException(ErrorCode.CONSTRAINT_VIOLATION, "Invalid Location UUID format");
        }

        log.debug("Looking for Location UUID: {}", locationUuid);
        Location location = locationRepository.findByUuid(locationUuid)
                .or(() -> locationRepository.findById(locationUuid))
                .orElseThrow(() -> {
                    log.error("Location NOT FOUND for UUID: {}", locationUuid);
                    return new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Location not found for UUID: " + locationUuid);
                });
        
        // 3. Create Asset
        Asset asset = new Asset();
        asset.setName(dto.name());
        asset.setInventoryNumber(dto.inventoryNumber());
        asset.setType(type);
        asset.setLocation(location);
        asset.setStatus(AssetStatus.IN_STOCK);
        asset.setCondition(dto.condition());
        asset.setAddedAt(java.time.LocalDateTime.now());
        asset.setPurchaseDate(dto.purchaseDate());
        asset.setWarrantyEnd(dto.warrantyEnd());

        Asset saved = assetRepository.save(asset);
        assetRepository.flush();

        historyService.logEvent(saved, "CREATED", "Asset created with initial status IN_STOCK", (Person) null);
        
        return assetMapper.toDto(saved);
    }
}
