package org.pawles.checkers.objects;

/**
 * Parent class for all piece objects
 * @author pawles
 * @author Szymon
 * @version 1.0
 */
public abstract class AbstractPiece {

    /** piece's position */
    protected transient Square square;

    /** piece's colour */
    protected final transient Colour colour;

    /**
     * construct the piece
     * @param square initial position
     * @param colour piece's colour
     */
    protected AbstractPiece(final Square square, final Colour colour) {
        this.square = square;
        this.colour = colour;
    }

    /**
     * checks if the piece has the ability to move between two squares
     */
    public abstract boolean verifyMove(Square dest);


    /**
     * updates piece's current square to a new one
     * @param dest destination square
     */
    public abstract void move(Square dest);

    public Square getSquare() {
        return square;
    }

    public Colour getColour() {
        return colour;
    }

    /**
     * checks whether the piece is a king
     * @param abstractPiece piece to check
     * @return true if the piece is a king; false otherwise
     */
    public boolean isKing(final AbstractPiece abstractPiece) {
        return King.class.isAssignableFrom(abstractPiece.getClass());
    }
}
