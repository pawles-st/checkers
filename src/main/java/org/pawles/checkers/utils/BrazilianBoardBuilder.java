package org.pawles.checkers.utils;

import org.pawles.checkers.objects.*;

import java.util.ArrayList;

public class BrazilianBoardBuilder extends BoardBuilder {
    @Override
    public void buildGrid() {
        ArrayList<ArrayList<Piece>> coordinates = new ArrayList<>(8);
        for (int i = 0; i < 8; ++i) {
            ArrayList<Piece> row = new ArrayList<>(8);
            coordinates.set(i, row);
        }
        board.setCoordinates(coordinates);
    }

    @Override
    public void buildPieces() {
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 3; ++y) {
                if ((x + y) % 2 == 0) {
                    board.getCoordinates().get(y).set(x, new Man(new Square(x, y), Colour.WHITE));
                }
            }
            for (int y = 5; y < 8; ++y) {
                if ((x + y) % 2 == 0) {
                    board.getCoordinates().get(y).set(x, new Man(new Square(x, y), Colour.BLACK));
                }
            }
        }
    }
}