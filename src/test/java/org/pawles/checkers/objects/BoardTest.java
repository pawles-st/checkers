package org.pawles.checkers.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private final Board board = new Board(8);

    @BeforeEach
    public void buildBoard() {
        SquareInstancer.initialise(8, 8);
        final List<List<AbstractPiece>> coordinates = new ArrayList<>();
        for (int y = 0; y < 8; ++y) {
            coordinates.add(new ArrayList<>());
            for (int x = 0; x < 8; ++x) {
                coordinates.get(y).add(null);
            }
        }
        board.setCoordinates(coordinates);
    }

    @Test
    public void manPromotionTest() {

        // add a single white piece and single black piece about to promote

        board.setCoordinate(SquareInstancer.getInstance(0, 6), new Man(SquareInstancer.getInstance(0, 6), Colour.WHITE));
        board.setCoordinate(SquareInstancer.getInstance(5, 1), new Man(SquareInstancer.getInstance(5, 1), Colour.BLACK));

        // move and promote the pawns

        board.movePiece(SquareInstancer.getInstance(0, 6), SquareInstancer.getInstance(1, 7));
        board.movePiece(SquareInstancer.getInstance(5, 1), SquareInstancer.getInstance(4, 0));

        // assert the pieces promoted

        assertInstanceOf(King.class, board.getCoordinates().get(7).get(1));
        assertInstanceOf(King.class, board.getCoordinates().get(0).get(4));
    }

    @Test
    public void pieceKillTest() {

        // add a single white piece and single black piece

        board.setCoordinate(SquareInstancer.getInstance(1, 1), new Man(SquareInstancer.getInstance(1, 1), Colour.WHITE));
        board.setCoordinate(SquareInstancer.getInstance(2, 2), new Man(SquareInstancer.getInstance(2, 2), Colour.BLACK));

        // kill the black piece with the white one

        board.movePiece(SquareInstancer.getInstance(1, 1), SquareInstancer.getInstance(3, 3));

        // assert the piece died

        assertNull(board.getCoordinates().get(2).get(2));
    }

    @Test
    public void moveVerificationTest() {

        // add a single white man and single black king

        board.setCoordinate(SquareInstancer.getInstance(3, 3), new Man(SquareInstancer.getInstance(3, 3), Colour.WHITE));
        board.setCoordinate(SquareInstancer.getInstance(7, 3), new King(SquareInstancer.getInstance(7, 3), Colour.BLACK));

        // correct moves. assert they work

        assertTrue(board.verifyMove(SquareInstancer.getInstance(3, 3), SquareInstancer.getInstance(4, 4)));
        assertTrue(board.verifyMove(SquareInstancer.getInstance(7, 3), SquareInstancer.getInstance(4, 0)));

        // incorrect. assert it fails

        assertFalse(board.verifyMove(SquareInstancer.getInstance(2, 1), SquareInstancer.getInstance(1, 2)));
    }

    @Test
    public void moveTest() {

        // add a single white piece

        board.setCoordinate(SquareInstancer.getInstance(4, 2), new Man(SquareInstancer.getInstance(4, 2), Colour.WHITE));

        // move the piece

        board.movePiece(SquareInstancer.getInstance(4, 2), SquareInstancer.getInstance(3, 3));

        // assert the board got updated correctly

        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 8; ++x) {
                if (x == 3 && y == 3) {
                    assertInstanceOf(Man.class, board.getCoordinates().get(x).get(y));
                } else {
                    assertNull(board.getCoordinates().get(y).get(x));
                }
            }
        }
    }
}