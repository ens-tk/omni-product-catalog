package org.example.exception;

public class ProductTimeoutException extends RuntimeException {

    public ProductTimeoutException(String message) {
        super(message);
    }
}