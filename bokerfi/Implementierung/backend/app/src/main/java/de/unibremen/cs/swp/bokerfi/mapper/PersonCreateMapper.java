package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper für PersonCreateDTO → Person.
 */

@Mapper(componentModel = "spring")
public interface PersonCreateMapper
        extends CreateMapper<Person, PersonCreateDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "role", expression = "java( resolveRole(dto) )")
    Person toEntity(PersonCreateDTO dto);

    default de.unibremen.cs.swp.bokerfi.model.Role resolveRole(PersonCreateDTO dto) {
        if (dto.role() != null) return dto.role();
        if (dto.roles() != null && !dto.roles().isEmpty()) return dto.roles().get(0);
        return de.unibremen.cs.swp.bokerfi.model.Role.EMPLOYEE; 
    }
}