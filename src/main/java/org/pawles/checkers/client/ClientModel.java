package org.pawles.checkers.client;

import org.pawles.checkers.objects.Board;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Square;
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

/**
 * MVC model class for the client side
 *
 * @author pawles
 * @version 1.0
 */
public class ClientModel {

    /** game board */
    final private Board board;

    /** player's colour */
    final private Colour colour;

    /**
     * initialise the MVC client's model
     * @param colour player's colour
     */
    public ClientModel(final Colour colour, int boardSize) {

        // build board

        final BoardDirector director = new BoardDirector();
        director.setBoardBuilder(new BrazilianBoardBuilder());
        director.buildBoard(boardSize);
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

    /**
     * place a piece at a given coordinate
     * @param square the square on the board to modify
     * @param piece the piece to place
     */
    public void setCoordinate(final Square square, final AbstractPiece piece) {
        board.setCoordinate(square, piece);
    }

    /**
     * check whether the piece has the ability to move between squares
     * @param curr current square
     * @param dest destination square
     * @return true if the move is correct; false otherwise
     */
    public boolean verifyMove(final Square curr, final Square dest) {
        return board.verifyMove(curr, dest);
    }

    /**
     * change the position of a single piece on the board
     * @param curr current square
     * @param dest destination square
     */
    public void movePiece(final Square curr, final Square dest) {
        board.movePiece(curr, dest);
    }
}
