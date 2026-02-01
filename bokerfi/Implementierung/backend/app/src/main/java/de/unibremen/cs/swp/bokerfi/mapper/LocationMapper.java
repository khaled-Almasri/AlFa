package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.LocationDTO;
import de.unibremen.cs.swp.bokerfi.model.Location;
import org.mapstruct.Mapper;

/**
 * Mapper für die Ausgabe von Standorten.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends DTOMapper<Location, LocationDTO> {
}
