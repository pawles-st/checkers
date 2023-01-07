package org.pawles.checkers.utils;

import org.pawles.checkers.objects.*;

import java.util.ArrayList;
import java.util.List;

public class BrazilianBoardBuilder extends AbstractBoardBuilder { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded

    @Override
    public void createNewBoard() {
        board = new Board(8);
    }

    @Override
    public void buildGrid() {
        final List<List<AbstractPiece>> coordinates = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            coordinates.add(new ArrayList<>());
        }
        board.setCoordinates(coordinates);
    }

    // can law of Demeter here be fixed?
    @Override
    public void buildPieces() { //NOPMD - suppressed CognitiveComplexity - method does a single job of filling the board with pieces
        int x; //NOPMD - suppressed ShortVariable - standerd coordinate name
        int y; //NOPMD - suppressed ShortVariable - standerd coordinate name
        for (x = 0; x < 8; ++x) {
            y = 0;

            // place white pieces

            for (; y < 3; ++y) {
                if ((x + y) % 2 == 0) {
                    board.getCoordinates().get(y).add(new Man(new Square(x, y), Colour.WHITE)); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - objects are created to be held
                } else {
                    board.getCoordinates().get(y).add(null);
                }
            }

            // empty two rows

            for (; y < 5; ++y) {
                board.getCoordinates().get(y).add(null);
            }

            // place black pieces

            for (; y < 8; ++y) {
                if ((x + y) % 2 == 0) {
                    board.getCoordinates().get(y).add(new Man(new Square(x, y), Colour.BLACK)); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - objects are created to be held
                } else {
                    board.getCoordinates().get(y).add(null);
                }
            }
        }
    }
}
