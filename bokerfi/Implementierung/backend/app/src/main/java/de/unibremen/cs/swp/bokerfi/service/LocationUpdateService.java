package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.LocationCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.LocationDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.mapper.LocationMapper;
import de.unibremen.cs.swp.bokerfi.model.Location;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import org.springframework.stereotype.Service;

/**
 * Service zum Aktualisieren bestehender Standorte.
 * <p>
 * Handhabt Aktualisierungen von Standort-Eigenschaften (Name).
 * </p>
 */
@Service
public class LocationUpdateService {

    private final LocationRepository repository;
    private final LocationMapper mapper;

    /**
     * Erstellt einen neuen LocationUpdateService.
     *
     * @param repository Das Standort-Repository
     * @param mapper     Der Standort-Mapper
     */
    public LocationUpdateService(LocationRepository repository, LocationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Aktualisiert einen Standort.
     *
     * @param id  Die ID des Standorts
     * @param dto  Das DTO mit den aktualisierten Daten
     * @return Das aktualisierte LocationDTO
     */


    @SuppressWarnings("null")
    public LocationDTO update(long id, LocationCreateDTO dto) {
        Location location = repository.findById(id)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Location not found"));

        if (dto.name() != null && !dto.name().isEmpty()) {
            location.setName(dto.name());
        }

        location = repository.save(location);
        return mapper.toDto(location);
    }
}
