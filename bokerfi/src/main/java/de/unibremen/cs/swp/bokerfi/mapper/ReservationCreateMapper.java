package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.ReservationCreateDTO;
import de.unibremen.cs.swp.bokerfi.model.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationCreateMapper {

    Reservation toEntity(ReservationCreateDTO dto);
}
