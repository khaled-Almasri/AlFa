package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.FacilityCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.FacilityDTO;
import de.unibremen.cs.swp.bokerfi.mapper.FacilityCreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.FacilityMapper;
import de.unibremen.cs.swp.bokerfi.model.Facility;
import de.unibremen.cs.swp.bokerfi.persistence.FacilityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FacilityCreateService {

    private final FacilityRepository repository;
    private final FacilityCreateMapper mapper;
    private final FacilityMapper responseMapper;

    public FacilityCreateService(
            FacilityRepository repository,
            FacilityCreateMapper mapper,
            FacilityMapper responseMapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.responseMapper = responseMapper;
    }

    public FacilityDTO createFacility(final FacilityCreateDTO facilityCreateDTO) {
        final Facility facility =
                repository.save(mapper.toEntity(facilityCreateDTO));

        log.info("Created facility with uuid {}", facility.getUuid());
        return responseMapper.toDto(facility);
    }
}
