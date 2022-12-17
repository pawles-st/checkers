package org.pawles.checkers.utils;

import org.pawles.checkers.objects.Board;

public abstract class BoardBuilder {
    protected Board board;

    public void createNewBoard() {
        board = new Board();
    }

    public Board getBoard() {
        return board;
    }

    public abstract void buildGrid();

    public abstract void buildPieces();
}
