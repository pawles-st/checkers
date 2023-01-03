package org.pawles.checkers.objects;

import java.util.List;

public class Board { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded
    private List<List<AbstractPiece>> coordinates;

    public List<List<AbstractPiece>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(final List<List<AbstractPiece>> coordinates) {
        this.coordinates = coordinates;
    }

    public void setCoordinate(final Square square, final AbstractPiece piece) {
        coordinates.get(square.getY()).set(square.getX(), piece);
    }

    public boolean verifyMove(final Square curr, final Square dest) {
        final AbstractPiece piece = coordinates.get(curr.getY()).get(curr.getX());
        boolean verify;
        if (piece == null) {
            verify = false;
        } else {
            verify = piece.verifyMove(dest);
        }
        return verify;
    }

    public void movePiece(final Square curr, final Square dest) {
        final AbstractPiece piece = coordinates.get(curr.getY()).get(curr.getX());
        piece.move(dest);
        coordinates.get(curr.getY()).set(curr.getX(), null);
        coordinates.get(dest.getY()).set(dest.getX(), piece);
    }

    public void deletePiece(final Square square) {
        coordinates.get(square.getY()).set(square.getX(), null);
    }
}
