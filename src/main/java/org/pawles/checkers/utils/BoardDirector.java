package org.pawles.checkers.utils;

import org.pawles.checkers.objects.Board;

/**
 * Director class for creating board using the builder pattern
 * @author pawles
 * @version 1.0
 */
public class BoardDirector { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded

    /** builder to use */
    private transient AbstractBoardBuilder builder;

    /**
     * assigns a specific builder to the director
     * @param builder the builder to use
     */
    public void setBoardBuilder(final AbstractBoardBuilder builder) {
        this.builder = builder;
    }

    public Board getBoard() { //NOPMD - suppressed CommentRequired - this is a simple getter method
        return builder.getBoard();
    }

    /**
     * initialises the board
     */
    public void buildBoard() {
        builder.createNewBoard();
        builder.buildGrid();
        builder.buildPieces();
    }
}
