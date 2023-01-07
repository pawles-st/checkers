package org.pawles.checkers.objects;

import java.util.List;

/**
 * Representation of the game board
 * @author pawles
 * @author Szymon
 * @version 1.0
 */
public class Board { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded

    /** coordinates list containing pieces */
    private List<List<AbstractPiece>> coordinates;

    public List<List<AbstractPiece>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(final List<List<AbstractPiece>> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * places a piece on a square
     * @param square square to place piece onto
     * @param piece piece to place
     */
    public void setCoordinate(final Square square, final AbstractPiece piece) {
        coordinates.get(square.getY()).set(square.getX(), piece); //NOPMD - suppressed LawOfDemeter - 2D array
    }

    /**
     * checks whether a piece can be moved between two squares
     * @param curr square to move from
     * @param dest square to move to
     * @return true if the move is possible; false otherwise
     */
    public boolean verifyMove(final Square curr, final Square dest) {
        final AbstractPiece piece = coordinates.get(curr.getY()).get(curr.getX()); //NOPMD - suppressed LawOfDemeter - 2D array
        boolean verify;
        if (piece == null) {
            verify = false;
        } else {
            verify = piece.verifyMove(dest);
        }
        return verify;
    }

    /**
     * kills appropriate pieces for the given move (only if a kill happened)
     * @param curr square a piece moved from
     * @param dest square a piece moved to
     */
    public void killPiece(final Square curr, final Square dest) {
        final int diff = Math.abs(curr.getX() - dest.getX());
        for (int i = 1; i < diff; ++i) {
            final int diffY = (dest.getY() - curr.getY()) / diff * i;
            final int diffX = (dest.getX() - curr.getX()) / diff * i;
            coordinates.get(curr.getY() + diffY).set(curr.getX() + diffX, null);
        }
    }

    /**
     * moves a single piece on the board
     * @param curr square the piece is currently on
     * @param dest destination square
     */
    public void movePiece(final Square curr, final Square dest) {
        final AbstractPiece piece = coordinates.get(curr.getY()).get(curr.getX()); //NOPMD - suppressed LawOfDemeter - 2D array
        piece.move(dest); //NOPMD - suppressed LawOfDemeter - 2D array
        coordinates.get(curr.getY()).set(curr.getX(), null); //NOPMD - suppressed LawOfDemeter - 2D array
        coordinates.get(dest.getY()).set(dest.getX(), piece); //NOPMD - suppressed LawOfDemeter - 2D array
        killPiece(curr, dest);
    }

    /**
     * removes piece from the board
     * @param square square to clear
     */
    public void deletePiece(final Square square) {
        coordinates.get(square.getY()).set(square.getX(), null); //NOPMD - suppressed LawOfDemeter - 2D array
    }
}
