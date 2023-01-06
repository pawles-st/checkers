package org.pawles.checkers.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.pawles.checkers.objects.Colour;

public class Tile extends Rectangle {
    public Tile(Colour colour, int x, int y) {
        setWidth(CheckersApp.TILE_SIZE);
        setHeight(CheckersApp.TILE_SIZE);

        relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);

        setFill(colour == Colour.BLACK ? Color.valueOf("#5C4033") : Color.valueOf("#C4A484"));
    }
}
