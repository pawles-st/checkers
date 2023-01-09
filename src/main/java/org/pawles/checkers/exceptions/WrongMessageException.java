package org.pawles.checkers.exceptions;

import java.io.Serial;

/**
 * Exception class to inform that server communication failed
 * @author pawles
 * @version 1.0
 */
public class WrongMessageException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 11L;

    /**
     * default constructor
     * @param message exception message
     */
    public WrongMessageException(final String message) {
        super(message);
    }
}