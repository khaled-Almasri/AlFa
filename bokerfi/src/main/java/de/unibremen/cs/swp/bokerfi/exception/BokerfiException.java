package de.unibremen.cs.swp.bokerfi.exception;

public class BokerfiException extends RuntimeException {

    private final ErrorCode errorCode;

    public BokerfiException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
