package org.pawles.checkers.objects;

import org.pawles.checkers.exceptions.IncorrectMoveException;

/**
 * King piece representation
 * @author pawles
 * @version 1.0
 */
public class King extends AbstractPiece { //NOPMD - suppressed ShortClassName - class name makes the most sense

    /**
     * constructs a king piece
     * @param square initial position
     * @param colour piece's colour
     */
    public King(final Square square, final Colour colour) {
        super(square, colour);
    }

    @Override
    public boolean verifyMove(final Square dest) {
        final int diffX = Math.abs(square.getX() - dest.getX());
        return diffX == Math.abs(square.getY() - dest.getY());
    }

    @Override
    public void move(final Square dest) {
        if(verifyMove(dest)) {
            square = SquareInstancer.getInstance(dest.getX(), dest.getY());
        } else {
            throw new IncorrectMoveException("The piece doesn't have the ability to move to the provided square");
        }
    }
}
