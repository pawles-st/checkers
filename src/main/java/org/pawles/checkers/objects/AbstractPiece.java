package org.pawles.checkers.objects;

public abstract class AbstractPiece {
    protected Square square;
    protected Colour colour;

    protected AbstractPiece(final Square square, final Colour colour) {
        this.square = square;
        this.colour = colour;
    }

    public abstract boolean verifyMove(Square dest);

    public abstract void move(Square dest);

    public Square getSquare() {
        return square;
    }

    public Colour getColour() {
        return colour;
    }
}
