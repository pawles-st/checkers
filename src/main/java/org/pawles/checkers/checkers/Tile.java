package org.pawles.checkers.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.pawles.checkers.objects.Colour;

/**
 * Tile class for GUI
 * @author Szymon
 * @author pawles
 * @version 1.0
 */
public class Tile extends Rectangle { //NOPMD - suppressed ShortClassName

    /**
     * constructs the tile object
     * @param colour tile colour
     * @param x tile x position
     * @param y tile y position
     */
    public Tile(final Colour colour, final int x, final int y) { //NOPMD - suppressed ShortVariable - standard coordinate names

        super();

        // set GUI size and position

        setWidth(CheckersApp.TILE_SIZE);
        setHeight(CheckersApp.TILE_SIZE);
        relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);

        // set colour

        setFill(colour == Colour.BLACK ? Color.valueOf("#5C4033") : Color.valueOf("#C4A484"));
    }
}
