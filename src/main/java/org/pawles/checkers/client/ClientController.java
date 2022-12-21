package org.pawles.checkers.client;

import org.pawles.checkers.objects.Piece;
import org.pawles.checkers.objects.Square;

public class ClientController {
    final private ClientModel model;
    final private ClientView view;

    public ClientController(ClientModel model, ClientView view) {
        this.model = model;
        this.view = view;
    }

    public void setCoordinate(Square square, Piece piece) {
        model.setCoordinate(square, piece);
    }

    public boolean verifyMove(Square curr, Square dest) {
        return model.verifyMove(curr, dest);
    }

    public void movePiece(Square curr, Square dest) {
        model.movePiece(curr, dest);
    }

    public void updateView() {
        view.drawBoard(model.getBoard());
    }
}
