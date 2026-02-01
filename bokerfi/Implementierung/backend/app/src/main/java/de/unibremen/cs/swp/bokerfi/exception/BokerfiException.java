package de.unibremen.cs.swp.bokerfi.exception;
/**
 * Zentrale fachliche Exception des Systems.
 *
 * <p>
 * Diese Exception wird verwendet, um fachliche Fehlerzustände
 * einheitlich darzustellen und über den globalen Exception-Handler
 * in HTTP-Fehlerantworten zu überführen.
 * </p>
 */

@SuppressWarnings("LombokGetterMayBeUsed")
public class BokerfiException extends RuntimeException {

    private final ErrorCode errorCode;

    public BokerfiException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public BokerfiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
