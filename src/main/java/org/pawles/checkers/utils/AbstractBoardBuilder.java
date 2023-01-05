package org.pawles.checkers.utils;

import org.pawles.checkers.objects.Board;

/**
 * Parent class for specific board builders
 * @author pawles
 * @version 1.0
 */
public abstract class AbstractBoardBuilder { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded

    /** game board */
    protected transient Board board;

    /**
     * constructs new board
     */
    public void createNewBoard() {
        board = new Board();
    }

    public Board getBoard() {
        return board;
    }

    /**
     * creates the board grid
     */
    public abstract void buildGrid();

    /**
     * adds pieces to the board
     */
    public abstract void buildPieces();
}
