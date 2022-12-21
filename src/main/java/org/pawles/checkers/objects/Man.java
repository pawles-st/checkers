package org.pawles.checkers.objects;

public class Man extends Piece {
    public Man(Square square, Colour colour) {
        super(square, colour);
    }

    @Override
    public boolean verifyMove(Square dest) {
        int diff_x = Math.abs(square.getX() - dest.getX());
        return diff_x == Math.abs(square.getY() - dest.getY()) && diff_x <= 2;
    }

    @Override
    public void move(Square dest) {
        if(verifyMove(dest)) {
            square.setX(dest.getX());
            square.setY(dest.getY());
        } else {
            throw new RuntimeException("Incorrect move");
        }
    }
}
