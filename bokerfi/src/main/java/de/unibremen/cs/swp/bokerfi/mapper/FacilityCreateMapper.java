package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.FacilityCreateDTO;
import de.unibremen.cs.swp.bokerfi.model.Facility;
import org.springframework.stereotype.Component;

@Component
public class FacilityCreateMapper {

    public Facility toEntity(FacilityCreateDTO dto) {
        Facility facility = new Facility();
        facility.setName(dto.name());
        return facility;
    }
}
