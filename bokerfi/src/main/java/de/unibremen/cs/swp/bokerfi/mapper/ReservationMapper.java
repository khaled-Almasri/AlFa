package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.ReservationDTO;
import de.unibremen.cs.swp.bokerfi.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "personUuid", source = "person.uuid")
    @Mapping(target = "facilityUuid", source = "facility.uuid")
    ReservationDTO toDto(Reservation reservation);

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "facility", ignore = true)
    @Mapping(target = "id", ignore = true)
    Reservation toEntity(ReservationDTO dto);
}
