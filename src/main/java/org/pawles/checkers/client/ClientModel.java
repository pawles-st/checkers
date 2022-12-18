package org.pawles.checkers.client;

import org.pawles.checkers.objects.Board;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Piece;
import org.pawles.checkers.objects.Square;
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

public class ClientModel {
    final private Board board;
    final private Colour colour;

    public ClientModel(Colour colour) {

        // build board

        BoardDirector director = new BoardDirector();
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

    public void setCoordinate(Square square, Piece piece) {
        board.setCoordinate(square, piece);
    }
}
