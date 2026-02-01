package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.AssetDTO;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper für die Konvertierung von Assets zwischen Entity und DTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, AssetTypeMapper.class, PersonMapper.class})

public interface AssetMapper extends DTOMapper<Asset, AssetDTO> {

    // Typname und Status explizit mappen
    @Override
    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "typeName", source = "type.name")
    @Mapping(target = "assigneeUuid", source = "assignedPerson.uuid")
    @Mapping(target = "locationUuid", source = "location.uuid")
    @Mapping(target = "typeUuid", source = "type.uuid")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "assignedPerson", source = "assignedPerson")
    @Mapping(target = "addedAt", source = "addedAt")
    @Mapping(target = "returnDeadline", expression = "java(getReturnDeadline(asset))")
    @Mapping(target = "assignedAt", expression = "java(getAssignedAt(asset))")
    AssetDTO toDto(Asset asset);

    default java.time.LocalDateTime getReturnDeadline(Asset asset) {
        if (asset.getAssignments() == null) return null;
        return asset.getAssignments().stream()
                .filter(a -> a.getReturnedAt() == null)
                .findFirst()
                .map(de.unibremen.cs.swp.bokerfi.model.Assignment::getReturnDeadline)
                .orElse(null);
    }

    default java.time.LocalDateTime getAssignedAt(Asset asset) {
        if (asset.getAssignments() == null) return null;
        return asset.getAssignments().stream()
                .filter(a -> a.getReturnedAt() == null)
                .findFirst()
                .map(de.unibremen.cs.swp.bokerfi.model.Assignment::getAssignedAt)
                .orElse(null);
    }

    default String mapStatus(de.unibremen.cs.swp.bokerfi.model.AssetStatus status) {
        return status != null ? status.name() : null;
    }
}
