package org.pawles.checkers.exceptions;

import java.io.Serial;

/**
 * Exception class for when an incorrect move happened
 * @author pawles
 * @version 1.0
 */
public class IncorrectMoveException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 12L;

    /**
     * default constructor
     * @param message exception message
     */
    public IncorrectMoveException(final String message) {
        super(message);
    }
}
