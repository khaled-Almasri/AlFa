package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.ReservationCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.ReservationDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.mapper.ReservationCreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.ReservationMapper;
import de.unibremen.cs.swp.bokerfi.model.Facility;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.model.Reservation;
import de.unibremen.cs.swp.bokerfi.persistence.FacilityRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import de.unibremen.cs.swp.bokerfi.persistence.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static de.unibremen.cs.swp.bokerfi.exception.ErrorCode.ENTITY_DOES_NOT_EXIST;

/**
 * Service zum Erstellen von Reservierungen.
 */
@Service
public class ReservationCreateService {

    private final ReservationRepository reservationRepository;
    private final PersonRepository personRepository;
    private final FacilityRepository facilityRepository;
    private final ReservationCreateMapper mapper;
    private final ReservationMapper responseMapper;

    public ReservationCreateService(
            ReservationRepository reservationRepository,
            PersonRepository personRepository,
            FacilityRepository facilityRepository,
            ReservationCreateMapper mapper,
            ReservationMapper responseMapper
    ) {
        this.reservationRepository = reservationRepository;
        this.personRepository = personRepository;
        this.facilityRepository = facilityRepository;
        this.mapper = mapper;
        this.responseMapper = responseMapper;
    }

    /**
     * Erstellt eine neue Reservierung.
     *
     * @param reservationCreateDTO DTO mit Zeitraum, Personen-UUID und Raum-UUID
     * @return gespeicherte Reservierung als DTO
     */
    @Transactional
    public ReservationDTO createReservation(
            final ReservationCreateDTO reservationCreateDTO
    ) {

        // DTO → Entity (ohne Referenzen)
        final Reservation reservation =
                mapper.toEntity(reservationCreateDTO);

        // Person anhand der UUID suchen
        final Optional<Person> optPerson =
                personRepository.findByUuid(reservationCreateDTO.personUuid());

        // Raum anhand der UUID suchen
        final Optional<Facility> optFacility =
                facilityRepository.findByUuid(reservationCreateDTO.facilityUuid());

        // Prüfen, ob beide existieren
        if (optPerson.isPresent() && optFacility.isPresent()) {
            reservation.setPerson(optPerson.get());
            reservation.setFacility(optFacility.get());
        } else {
            throw new BokerfiException(ENTITY_DOES_NOT_EXIST);
        }

        // Reservierung speichern
        final Reservation newReservation =
                reservationRepository.save(reservation);

        // Entity → DTO
        return responseMapper.toDto(newReservation);
    }
}
