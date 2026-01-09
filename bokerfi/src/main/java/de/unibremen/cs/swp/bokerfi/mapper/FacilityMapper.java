package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.FacilityDTO;
import de.unibremen.cs.swp.bokerfi.model.Facility;
import org.springframework.stereotype.Component;

@Component
public class FacilityMapper {

    public FacilityDTO toDto(Facility facility) {
        return new FacilityDTO(
                facility.getName(),
                facility.getUuid()
        );
    }
}
