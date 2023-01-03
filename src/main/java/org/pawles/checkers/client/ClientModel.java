package org.pawles.checkers.client;

import org.pawles.checkers.objects.Board;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Square;
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

public class ClientModel {
    final private Board board;
    final private Colour colour;

    public ClientModel(final Colour colour) {

        // build board

        final BoardDirector director = new BoardDirector();
        director.setBoardBuilder(new BrazilianBoardBuilder());
        director.buildBoard();
        board = director.getBoard();

        // set colour

        this.colour = colour;
    }

    public Board getBoard() {
        return board;
    }

    public Colour getColour() {
        return colour;
    }

    public void setCoordinate(final Square square, final AbstractPiece piece) {
        board.setCoordinate(square, piece);
    }

    public boolean verifyMove(final Square curr, final Square dest) {
        return board.verifyMove(curr, dest);
    }

    public void movePiece(final Square curr, final Square dest) {
        board.movePiece(curr, dest);
    }
}
