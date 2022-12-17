package org.pawles.checkers.objects;

public abstract class Piece {
    private Square square;
    private Colour colour;

    protected Piece(Colour colour) {
        this.colour = colour;
    }
}
