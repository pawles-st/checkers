package org.pawles.checkers.exceptions;

import java.io.Serial;

/**
 * Exception class for when an incorrect piece was passed
 * @author pawles
 * @version 1.0
 */
public class UnknownPieceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 13L;

    /**
     * default constructor
     * @param message exception message
     */
    public UnknownPieceException(final String message) {
        super(message);
    }
}