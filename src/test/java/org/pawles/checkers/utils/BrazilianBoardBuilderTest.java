package org.pawles.checkers.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Board;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Man;

import static org.junit.jupiter.api.Assertions.*;

class BrazilianBoardBuilderTest {

    private static Board board;

    @BeforeAll
    public static void init() {
        final BoardDirector director = new BoardDirector();
        director.setBoardBuilder(new BrazilianBoardBuilder());
        director.buildBoard(8);
        board = director.getBoard();
    }

    @Test
    public void boardSizeTest() {
        assertEquals(8, board.getCoordinates().size());
        for (int y = 0; y < 8; ++y) {
            assertEquals(8, board.getCoordinates().get(y).size());
        }
    }

    @Test
    public void boardPiecesTest() {
        final AbstractPiece whitePiece = board.getCoordinates().get(2).get(2);
        assertTrue(whitePiece instanceof Man && whitePiece.getColour() == Colour.WHITE);

        final AbstractPiece blackPiece = board.getCoordinates().get(7).get(5);
        assertTrue(blackPiece instanceof Man && blackPiece.getColour() == Colour.BLACK);
    }
}