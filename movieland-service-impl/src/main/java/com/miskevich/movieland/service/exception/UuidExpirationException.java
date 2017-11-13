package com.miskevich.movieland.service.exception;

public class UuidExpirationException extends RuntimeException {
    public UuidExpirationException(String message) {
        super(message);
    }
}
