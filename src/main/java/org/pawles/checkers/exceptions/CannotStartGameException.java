package org.pawles.checkers.exceptions;

public class CannotStartGameException extends RuntimeException {
    public CannotStartGameException(String message) {
        super(message);
    }
}
