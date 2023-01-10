package org.pawles.checkers.utils;

import java.security.InvalidParameterException;

/**
 * Factory method class for creating a board builder
 * @author pawles
 * @version 1.0
 */
public class BoardBuilderFactory {  //NOPMD - suppressed AtLeastOneConstructor - cter unneeded

    /**
     * creates a board builder
     * @param builder type of the builder
     * @return requested board builder object
     */
    public AbstractBoardBuilder createBuilder(final String builder) {
        if ("Brazilian".equals(builder)) {
            return new BrazilianBoardBuilder();
        } else {
            throw new InvalidParameterException("invalid board builder");
        }
    }
}
