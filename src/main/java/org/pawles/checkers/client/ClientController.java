package org.pawles.checkers.client;

import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Square;

public class ClientController {
    final private ClientModel model;
    final private ClientView view;

    public ClientController(final ClientModel model, final ClientView view) {
        this.model = model;
        this.view = view;
    }

    public void setCoordinate(final Square square, final AbstractPiece piece) {
        model.setCoordinate(square, piece);
    }

    public boolean verifyMove(final Square curr, final Square dest) {
        return model.verifyMove(curr, dest);
    }

    public void movePiece(final Square curr, final Square dest) {
        model.movePiece(curr, dest);
    }

    public void updateView() {
        view.drawBoard(model.getBoard());
    }

    public Colour getColour() {
        return model.getColour();
    }
}
