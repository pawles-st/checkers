package org.pawles.checkers.exceptions;

import java.io.Serial;

/**
 * Exception class to inform the client that the game cannot start
 * @author pawles
 * @version 1.0
 */
public class CannotStartGameException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 10L;

    /**
     * default constructor
     * @param message exception message
     */
    public CannotStartGameException(final String message) {
        super(message);
    }
}
