package org.pawles.checkers.client;

import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Square;

/**
 * MVC Controller class for the game from the client side
 *
 * @author pawles
 * @version 1.0
 */
public class ClientController {

    /** MVC client model object */
    final private transient ClientModel model;

    /** MVC client view object */
    final private transient ClientView view;

    /**
     * initialise the controller
     * @param model MVC client model object
     * @param view MVC client view object
     */
    public ClientController(final ClientModel model, final ClientView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * place a piece at a given coordinate
     * @param square the square on the board to modify
     * @param piece the piece to place
     */
    public void setCoordinate(final Square square, final AbstractPiece piece) {
        model.setCoordinate(square, piece);
    }

    /**
     * check whether the piece has the ability to move between squares
     * @param curr current square
     * @param dest destination square
     * @return true if the move is correct; false otherwise
     */
    public boolean verifyMove(final Square curr, final Square dest) {
        return model.verifyMove(curr, dest);
    }

    /**
     * change the position of a single piece on the board
     * @param curr current square
     * @param dest destination square
     */
    public void movePiece(final Square curr, final Square dest) {
        model.movePiece(curr, dest);
    }

    /**
     * draw the current board
     */
    public void updateView() {
        view.drawBoard(model.getBoard());
    }

    /**
     * return player's colour
     * @return player's colour
     */
    public Colour getColour() {
        return model.getColour();
    }
}
