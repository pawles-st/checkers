package org.pawles.checkers.utils;

import org.pawles.checkers.objects.*;

import java.util.ArrayList;
import java.util.List;

public class BrazilianBoardBuilder extends BoardBuilder {
    @Override
    public void buildGrid() {
        List<List<Piece>> coordinates = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            coordinates.add(new ArrayList<>());
        }
        board.setCoordinates(coordinates);
    }

    @Override
    public void buildPieces() {
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 3; ++y) {
                if ((x + y) % 2 == 0) {
                    board.getCoordinates().get(y).add(new Man(new Square(x, y), Colour.WHITE));
                } else {
                    board.getCoordinates().get(y).add(null);
                }
            }
            for (int y = 3; y < 5; ++y) {
                board.getCoordinates().get(y).add(null);
            }
            for (int y = 5; y < 8; ++y) {
                if ((x + y) % 2 == 0) {
                    board.getCoordinates().get(y).add(new Man(new Square(x, y), Colour.BLACK));
                } else {
                    board.getCoordinates().get(y).add(null);
                }
            }
        }
    }
}
