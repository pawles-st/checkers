package org.pawles.checkers.objects;

public abstract class Piece {
    protected Square square;
    protected Colour colour;

    protected Piece(Square square, Colour colour) {
        this.square = square;
        this.colour = colour;
    }

    public abstract void move(Square curr, Square dest);

    public Square getSquare() {
        return square;
    }

    public Colour getColour() {
        return colour;
    }
}
