package org.pawles.checkers.objects;

public class Man extends AbstractPiece {
    public Man(final Square square, final Colour colour) {
        super(square, colour);
    }

    @Override
    public boolean verifyMove(final Square dest) {
        final int diffX = Math.abs(square.getX() - dest.getX());
        return diffX == Math.abs(square.getY() - dest.getY()) && diffX <= 2;
    }

    @Override
    public void move(final Square dest) {
        if(verifyMove(dest)) {
            square.setX(dest.getX());
            square.setY(dest.getY());
        } else {
            throw new RuntimeException("Incorrect move");
        }
    }
}
