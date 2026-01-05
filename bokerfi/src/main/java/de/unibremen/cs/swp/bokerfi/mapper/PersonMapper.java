package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper für die Ausgabe von Personen.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDTO toDto(Person person);

    List<PersonDTO> toDtoList(List<Person> persons);
}
