package org.pawles.checkers.objects;

public class Man extends Piece {
    public Man(Square square, Colour colour) {
        super(square, colour);
    }

    @Override
    public void move(Square curr, Square dest) {
        int diff_x = curr.getX() - dest.getX();
        if (diff_x != curr.getY() - dest.getY() || diff_x > 2) {
            // illegal move, throw exception
        } else {
            square.setX(dest.getX());
            square.setY(dest.getY());
        }
    }
}
