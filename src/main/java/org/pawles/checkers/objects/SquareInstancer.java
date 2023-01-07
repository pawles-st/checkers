package org.pawles.checkers.objects;

import java.util.ArrayList;

public final class SquareInstancer {
    private static final ArrayList<ArrayList<Square>> squares = new ArrayList<>();
    public static void initialise(final int sizeX, final int sizeY) {
        for (int y = 0; y < sizeY; ++y) {
            ArrayList<Square> row = new ArrayList<>();
            for (int x = 0; x < sizeX; ++x) {
                row.add(new Square(x, y));
            }
            squares.add(row);
        }
    }

    public static Square getInstance(final int x, final int y) {
        return squares.get(y).get(x);
    }
}
