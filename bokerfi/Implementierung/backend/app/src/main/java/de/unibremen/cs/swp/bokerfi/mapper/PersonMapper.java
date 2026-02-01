package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.model.Person;
import org.mapstruct.Mapper;

/**
 * Mapper für die Ausgabe von Personen.
 */

@Mapper(componentModel = "spring")
public interface PersonMapper extends DTOMapper<Person, PersonDTO> {
    @Override
    @org.mapstruct.Mapping(target = "roles", expression = "java(java.util.List.of(person.getRole().name()))")
    PersonDTO toDto(Person person);
}

