package org.pawles.checkers.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton-like pattern class that holds all squares on the board
 * @author pawles
 * @version 1.0
 */
public final class SquareInstancer {

    /** list of squares on the board */
    private static final List<List<Square>> SQUARES = new ArrayList<>();

    private SquareInstancer() {}

    /**
     * sets the size of the board
     * @param width width of the board
     * @param height height of the board
     */
    public static void initialise(final int width, final int height) {
        for (int y = 0; y < height; ++y) {
            final ArrayList<Square> row = new ArrayList<>(); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - loop exists for the purpose of initialisation
            for (int x = 0; x < width; ++x) {
                row.add(new Square(x, y));
            }
            SQUARES.add(row);
        }
    }

    public static Square getInstance(final int x, final int y) { //NOPMD - suppressed ShortVariable - standard names for coordinates
        return SQUARES.get(y).get(x); //NOPMD - suppressed LawOfDemeter - 2D array
    }
}
