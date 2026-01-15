package dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.exceptions;

public class DomainException extends RuntimeException{
    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
