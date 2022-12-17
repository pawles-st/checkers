package org.pawles.checkers.utils;

import org.pawles.checkers.objects.Board;

public class BoardDirector {
    private BoardBuilder builder;

    public void setBoardBuilder(BoardBuilder builder) {
        this.builder = builder;
    }

    public Board getBoard() {
        return builder.getBoard();
    }

    public void buildBoard() {
        builder.buildGrid();
        builder.buildPieces();
    }
}
