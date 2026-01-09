package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.PersonUpdateDTO;
import de.unibremen.cs.swp.bokerfi.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonUpdateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "email", ignore = true)
    void applyUpdate(PersonUpdateDTO dto, @MappingTarget Person person);
}
