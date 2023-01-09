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

    private void buildWhite(final int x) { //NOPMD - suppressed ShortVariable - standard name
        for (int y = 0; y < 3; ++y) {
            if ((x + y) % 2 == 0) {
                board.getCoordinates().get(y).add(new Man(SquareInstancer.getInstance(x, y), Colour.WHITE)); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - objects are created to be held
            } else {
                board.getCoordinates().get(y).add(null); //NOPMD - suppressed LawOfDemeter - Array usage
            }
        }
    }

    private void buildEmpty() {
        for (int y = 3; y < 6; ++y) {
            board.getCoordinates().get(y).add(null); //NOPMD - suppressed LawOfDemeter - Array usage
        }
    }

    private void buildBlack(final int x) { //NOPMD - suppressed ShortVariable - standard name
        for (int y = 5; y < 8; ++y) {
            if ((x + y) % 2 == 0) {
                board.getCoordinates().get(y).add(new Man(SquareInstancer.getInstance(x, y), Colour.BLACK)); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - objects are created to be held
            } else {
                board.getCoordinates().get(y).add(null); //NOPMD - suppressed LawOfDemeter - Array usage
            }
        }
    }

    @Override
    public void createNewBoard() {
        board = new Board(8);
    }

    @Override
    public void buildGrid() {
        SquareInstancer.initialise(8, 8);
        final List<List<AbstractPiece>> coordinates = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            coordinates.add(new ArrayList<>());
        }
        board.setCoordinates(coordinates);
    }

    // can law of Demeter here be fixed?
    @Override
    public void buildPieces() {
        int x; //NOPMD - suppressed ShortVariable - standard coordinate name
        for (x = 0; x < 8; ++x) {

            // place white pieces

            buildWhite(x);


            // empty two rows

            buildEmpty();

            // place black pieces

            buildBlack(x);
        }
    }
}
