package org.pawles.checkers.utils;

import org.pawles.checkers.objects.*; //NOPMD - suppressed UnusedImports - these imports are used

import java.util.ArrayList;
import java.util.List;

/**
 * Specific builder class for creating a Brazilian board
 * @author pawles
 * @version 1.0
 */
public class BrazilianBoardBuilder extends AbstractBoardBuilder { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded

    /** height of the board */
    private static final int HEIGHT = 8;

    /** widht of the board */
    private static final int WIDTH = 8;

    /** y coordinate for where the white pieces start */
    private static final int WHITE_LINE_START = 0;

    /** y coordinate for where the white pieces end */
    private static final int WHITE_LINE_END = 2;

    /** y coordinate for where the black pieces start */
    private static final int BLACK_LINE_START = 5;

    /** y coordinate for where the black pieces end */
    private static final int BLACK_LINE_END = 7;

    private void buildWhite(final int x) { //NOPMD - suppressed ShortVariable - standard name
        for (int y = WHITE_LINE_START; y <= WHITE_LINE_END; ++y) {
            if ((x + y) % 2 == 0) {
                board.getCoordinates().get(y).add(new Man(SquareInstancer.getInstance(x, y), Colour.WHITE)); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - objects are created to be held
            } else {
                board.getCoordinates().get(y).add(null); //NOPMD - suppressed LawOfDemeter - Array usage
            }
        }
    }

    private void buildEmpty() {
        for (int y = WHITE_LINE_END + 1; y <= BLACK_LINE_START - 1; ++y) {
            board.getCoordinates().get(y).add(null); //NOPMD - suppressed LawOfDemeter - Array usage
        }
    }

    private void buildBlack(final int x) { //NOPMD - suppressed ShortVariable - standard name
        for (int y = BLACK_LINE_START; y <= BLACK_LINE_END; ++y) {
            if ((x + y) % 2 == 0) {
                board.getCoordinates().get(y).add(new Man(SquareInstancer.getInstance(x, y), Colour.BLACK)); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - objects are created to be held
            } else {
                board.getCoordinates().get(y).add(null); //NOPMD - suppressed LawOfDemeter - Array usage
            }
        }
    }

    @Override
    public void createNewBoard() {
        board = new Board(HEIGHT);
    }

    @Override
    public void buildGrid() {
        SquareInstancer.initialise(WIDTH, HEIGHT);
        final List<List<AbstractPiece>> coordinates = new ArrayList<>();
        for (int y = 0; y < HEIGHT; ++y) {
            coordinates.add(new ArrayList<>());
        }
        board.setCoordinates(coordinates);
    }

    // can law of Demeter here be fixed?
    @Override
    public void buildPieces() {
        int x; //NOPMD - suppressed ShortVariable - standard coordinate name
        for (x = 0; x < WIDTH; ++x) {

            // place white pieces

            buildWhite(x);


            // empty two rows

            buildEmpty();

            // place black pieces

            buildBlack(x);
        }
    }
}
