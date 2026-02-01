package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.LocationDTO;
import de.unibremen.cs.swp.bokerfi.mapper.LocationMapper;
import de.unibremen.cs.swp.bokerfi.model.Location;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import de.unibremen.cs.swp.bokerfi.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Service für allgemeine Standort-Operationen.
 * <p>
 * Bietet Lesezugriff auf Standorte.
 * </p>
 */
@Service
public class LocationService extends BaseService<Location, LocationDTO, LocationRepository> {
    /**
     * Erstellt einen neuen LocationService.
     *
     * @param repository Das Standort-Repository
     * @param mapper     Der Standort-Mapper
     */
    public LocationService(LocationRepository repository, LocationMapper mapper) {
        super(repository, mapper);
    }
}
