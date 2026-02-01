package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.LocationCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.LocationDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.mapper.LocationCreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.LocationMapper;
import de.unibremen.cs.swp.bokerfi.model.Location;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import org.springframework.stereotype.Service;

/**
 * Service zum Erstellen neuer Standorte.
 * <p>
 * Handhabt die Validierung und Erstellung von Standorten.
 * </p>
 */
@Service
@org.springframework.transaction.annotation.Transactional
public class LocationCreateService {
    private final LocationRepository repository;
    private final LocationCreateMapper createMapper;
    private final LocationMapper responseMapper;

    /**
     * Erstellt einen neuen LocationCreateService.
     *
     * @param repository     Das Standort-Repository
     * @param createMapper   Der Create-Mapper
     * @param responseMapper Der Response-Mapper
     */
    public LocationCreateService(LocationRepository repository, LocationCreateMapper createMapper, LocationMapper responseMapper) {
        this.repository = repository;
        this.createMapper = createMapper;
        this.responseMapper = responseMapper;
    }

    /**
     * Erstellt einen neuen Standort.
     *
     * @param dto Das DTO mit den Standort-Daten
     * @return Das erstellte LocationDTO
     */
    @SuppressWarnings("null")
    public LocationDTO create(LocationCreateDTO dto) {
        if (repository.findByName(dto.name()).isPresent()) {
             throw new BokerfiException(ErrorCode.ENTITY_ALREADY_EXISTS, "Location with name " + dto.name() + " already exists");
        }
        Location location = createMapper.toEntity(dto);
        location = repository.save(location);
        repository.flush();
        return responseMapper.toDto(location);
    }
}
