package dev.pedroscheurer.nambadelivery.delivery.tracking.infrastructure.http.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class BadGatewayException extends RuntimeException{
    public BadGatewayException() {
        super();
    }

    public BadGatewayException(Throwable cause) {
        super(cause);
    }
}
