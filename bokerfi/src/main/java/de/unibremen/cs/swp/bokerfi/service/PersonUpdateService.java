package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonUpdateDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.mapper.PersonUpdateMapper;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonUpdateService {

    private final PersonRepository repository;
    private final PersonUpdateMapper updateMapper;
    private final PersonMapper personMapper;

    public PersonUpdateService(PersonRepository repository,
                               PersonUpdateMapper updateMapper,
                               PersonMapper personMapper) {
        this.repository = repository;
        this.updateMapper = updateMapper;
        this.personMapper = personMapper;
    }

    public PersonDTO applyUpdate(PersonUpdateDTO dto, Long uuid) {
        Person person = repository.findByUuid(uuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST));

        updateMapper.applyUpdate(dto, person);
        return personMapper.toDto(repository.save(person));
    }
}
