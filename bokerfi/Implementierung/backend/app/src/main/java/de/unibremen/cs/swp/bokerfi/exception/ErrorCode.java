package de.unibremen.cs.swp.bokerfi.exception;


/**
 * Definition der möglichen Fehlercodes.
 */
public enum ErrorCode {
    ENTITY_DOES_NOT_EXIST,
    ENTITY_ALREADY_EXISTS,
    RESERVATION_CONFLICT,
    INVALID_TIME_RANGE,
    VERSION_CONFLICT,
    ASSET_NOT_AVAILABLE,
    INVALID_STATUS_TRANSITION,
    ASSET_BLOCKED,
    CONSTRAINT_VIOLATION,
    CONFLICT,
    INTERNAL_SERVER_ERROR
}
