package de.unibremen.cs.swp.bokerfi.persistence;

import de.unibremen.cs.swp.bokerfi.model.DBEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Basis-Repository-Interface für gemeinsame Operationen.
 *
 * @param <E> Der Entitätstyp
 */
@NoRepositoryBean
public interface BaseRepository<E extends DBEntity> extends JpaRepository<E, Long> {

    Optional<E> findByUuid(long uuid);

    void deleteByUuid(Long uuid);
}
