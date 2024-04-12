package com.grigoryev.rest.handler;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(StatusRuntimeException.class)
    public Mono<ResponseEntity<ExceptionResponse>> handleStatusRuntimeException(StatusRuntimeException e) {
        Status.Code code = e.getStatus().getCode();
        HttpStatus httpStatus;
        switch (code) {
            case NOT_FOUND -> httpStatus = HttpStatus.NOT_FOUND;
            case UNAUTHENTICATED -> httpStatus = HttpStatus.UNAUTHORIZED;
            default -> httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return Mono.just(ResponseEntity.status(httpStatus).body(new ExceptionResponse(e.getMessage())))
                .log();
    }

}
