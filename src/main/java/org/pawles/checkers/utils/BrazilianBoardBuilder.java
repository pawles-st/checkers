package org.pawles.checkers.utils;

import org.pawles.checkers.objects.*; //NOPMD - suppressed UnusedImports - these imports are used

import java.util.ArrayList;
import java.util.List;

/**
 * Specific builder class for creating a Brazilian board
 * @author pawles
 * @author Szymon
 * @version 1.0
 */
public class BrazilianBoardBuilder extends AbstractBoardBuilder { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded

    /* height of the board */
    //private static final int HEIGHT = 8;

    /* widht of the board */
    //private static final int WIDTH = 8;

    /* y coordinate for where the white pieces start */
    //private static final int WHITE_LINE_START = 0;

    /* y coordinate for where the white pieces end */
    //private static final int WHITE_LINE_END = 2;

    /* y coordinate for where the black pieces start */
    //private static final int BLACK_LINE_START = 5;

    /* y coordinate for where the black pieces end */
    //private static final int BLACK_LINE_END = 7;

    private void buildWhite(final int x, int boardSize) { //NOPMD - suppressed ShortVariable - standard name
        final int whiteLineStart = 0; //NOPMD - suppressed AvoidFinalLocalVariable
        final int whiteLineEnd = boardSize / 2 - 2;
        for (int y = whiteLineStart; y <= whiteLineEnd; ++y) {
            if ((x + y) % 2 == 0) {
                board.getCoordinates().get(y).add(new Man(SquareInstancer.getInstance(x, y), Colour.WHITE)); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - objects are created to be held
            } else {
                board.getCoordinates().get(y).add(null); //NOPMD - suppressed LawOfDemeter - Array usage
            }
        }
    }

    private void buildEmpty(final int boardSize) {
        final int whiteLineEnd = boardSize / 2 - 2;
        final int blackLineStart = boardSize / 2 + 1;
        for (int y = whiteLineEnd + 1; y <= blackLineStart - 1; ++y) {
            board.getCoordinates().get(y).add(null); //NOPMD - suppressed LawOfDemeter - Array usage
        }
    }

    private void buildBlack(final int x, int boardSize) { //NOPMD - suppressed ShortVariable - standard name
        final int blackLineStart = boardSize / 2 + 1;
        final int blackLineEnd = boardSize - 1;
        for (int y = blackLineStart; y <= blackLineEnd; ++y) {
            if ((x + y) % 2 == 0) {
                board.getCoordinates().get(y).add(new Man(SquareInstancer.getInstance(x, y), Colour.BLACK)); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - objects are created to be held
            } else {
                board.getCoordinates().get(y).add(null); //NOPMD - suppressed LawOfDemeter - Array usage
            }
        }
    }

    @Override
    public void createNewBoard(final int boardSize) {
        board = new Board(boardSize);
    }

    @Override
    public void buildGrid(final int boardSize) {
        SquareInstancer.initialise(boardSize, boardSize);
        final List<List<AbstractPiece>> coordinates = new ArrayList<>();
        for (int y = 0; y < boardSize; ++y) {
            coordinates.add(new ArrayList<>());
        }
        board.setCoordinates(coordinates);
    }

    // can law of Demeter here be fixed?
    @Override
    public void buildPieces(final int boardSize) {
        int x; //NOPMD - suppressed ShortVariable - standard coordinate name
        for (x = 0; x < boardSize; ++x) {

            // place white pieces

            buildWhite(x, boardSize);


            // empty two rows

            buildEmpty(boardSize);

            // place black pieces

            buildBlack(x, boardSize);
        }
    }
}
