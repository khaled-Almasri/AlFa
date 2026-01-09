package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
