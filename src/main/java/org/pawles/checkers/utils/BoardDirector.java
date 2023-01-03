package org.pawles.checkers.utils;

import org.pawles.checkers.objects.Board;

public class BoardDirector { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded
    private AbstractBoardBuilder builder;

    public void setBoardBuilder(final AbstractBoardBuilder builder) {
        this.builder = builder;
    }

    public Board getBoard() {
        return builder.getBoard();
    }

    public void buildBoard() {
        builder.createNewBoard();
        builder.buildGrid();
        builder.buildPieces();
    }
}
