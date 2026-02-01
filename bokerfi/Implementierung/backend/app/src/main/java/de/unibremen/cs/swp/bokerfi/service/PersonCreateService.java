package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.mapper.PersonCreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import de.unibremen.cs.swp.bokerfi.service.base.BaseCreateService;
import org.springframework.stereotype.Service;

/**
 * Service zum Erstellen neuer Personen.
 * <p>
 * Handhabt die Validierung und Erstellung von Benutzern.
 * </p>
 */
@Service
public class PersonCreateService extends BaseCreateService<
        Person,
        PersonCreateDTO,
        PersonDTO,
        PersonRepository> {

    /**
     * Erstellt einen neuen PersonCreateService.
     *
     * @param repository     Das Personen-Repository
     * @param createMapper   Der Create-Mapper
     * @param responseMapper Der Response-Mapper
     */
    public PersonCreateService(PersonRepository repository,
                               PersonCreateMapper createMapper,
                               PersonMapper responseMapper) {
        super(repository, createMapper, responseMapper);
    }

    @Override
    @SuppressWarnings("java:S2589")
    protected void validate(Person entity, PersonCreateDTO dto) {
        if (repository.findByEmailIgnoreCase(dto.email().trim()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (dto.personnelNumber() != null && repository.findByPersonnelNumberIgnoreCase(dto.personnelNumber().trim()).isPresent()) {
            throw new IllegalArgumentException("Personnel Number already exists");
        }
    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    @SuppressWarnings({"java:S2589", "java:S2583"})
    public PersonDTO create(PersonCreateDTO createDTO) {
         // Sanitize input
        PersonCreateDTO sanitized = new PersonCreateDTO(
                createDTO.email().trim(),
                createDTO.firstName(),
                createDTO.lastName(),
                createDTO.personnelNumber() != null ? createDTO.personnelNumber().trim() : null,
                createDTO.password(),
                createDTO.role(),
                createDTO.roles()
        );
        PersonDTO dto = super.create(sanitized);
        repository.flush();
        return dto;
    }
}
