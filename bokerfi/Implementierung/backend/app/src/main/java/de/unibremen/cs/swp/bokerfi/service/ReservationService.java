package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.ReservationCreateDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Location;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.model.Reservation;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import de.unibremen.cs.swp.bokerfi.persistence.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service für Reservierungen.
 */
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository repository;
    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;

    public ReservationService(ReservationRepository repository, PersonRepository personRepository, LocationRepository locationRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
    }

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public List<Reservation> findAllByPerson(Person person) {
        return repository.findAllByPerson(person);
    }

    public List<Reservation> findAllByPersonEmail(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "User not found"));
        return repository.findAllByPerson(person);
    }

    public Long create(ReservationCreateDTO dto) {
        if (dto.start() == null || dto.end() == null) {
            throw new BokerfiException(ErrorCode.CONSTRAINT_VIOLATION, "Start and End required");
        }
        if (dto.end().isBefore(dto.start())) {
            throw new BokerfiException(ErrorCode.CONSTRAINT_VIOLATION, "End before Start");
        }

        Person person = null;
        if (dto.personUuid() != null) {
            long pUuid = Long.parseLong(dto.personUuid());
            person = personRepository.findByUuid(pUuid)
                    .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Person not found"));
        }

        Location location = null;
        if (dto.locationUuid() != null) {
            long lUuid = Long.parseLong(dto.locationUuid());
            location = locationRepository.findByUuid(lUuid)
                    .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Location not found"));
        }

        Reservation reservation = new Reservation();
        reservation.setStart(dto.start());
        reservation.setEnd(dto.end());
        reservation.setPerson(person);
        reservation.setLocation(location);
        reservation.setReason(dto.reason());

        repository.save(reservation);
        return reservation.getUuid();
    }

    @SuppressWarnings("null")
    public void delete(long uuid) {
        Reservation reservation = repository.findByUuid(uuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Reservation not found"));
        repository.delete(reservation);
    }
}
