package de.unibremen.cs.swp.bokerfi.mapper;

/**
 * Generischer Mapper für Erstellungsoperationen (CreateDTO -> Entity).
 *
 * @param <E>    Der Entitätstyp
 * @param <CDTO> Der Create-DTO-Typ
 */
public interface CreateMapper<E, C> {
    E toEntity(C dto);
}