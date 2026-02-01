package de.unibremen.cs.swp.bokerfi.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Generischer Mapper für DTO-Konvertierungen.
 *
 * @param <E> Der Entitätstyp
 * @param <D> Der DTO-Typ
 */
public interface DTOMapper<E, D> {

    D toDto(E source);

    // MapStruct-freundlich
    default List<D> toDtoList(List<E> source) {
        return source.stream()
                .map(this::toDto)
                .toList();
    }

    //  WICHTIG: default → MapStruct MUSS nichts generieren
    default E applyUpdate(D source, @MappingTarget E target) {
        // bewusst leer – konkrete Update-Logik ist optional
        return target;
    }
}
