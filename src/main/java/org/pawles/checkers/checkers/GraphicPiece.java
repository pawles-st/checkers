package org.pawles.checkers.checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import org.pawles.checkers.objects.Colour;

import static org.pawles.checkers.checkers.CheckersApp.TILE_SIZE;

public class GraphicPiece extends StackPane {
    public GraphicPiece(Colour color, int x, int y) {
        relocate(x * TILE_SIZE, y*TILE_SIZE);
        Ellipse shape = new Ellipse(TILE_SIZE*0.3125, TILE_SIZE*0.26);
        shape.setFill(color == Colour.WHITE ? Color.valueOf("#000") : Color.valueOf("#FFF"));
        shape.setStroke(Color.GRAY);
        shape.setStrokeWidth(TILE_SIZE*0.03);
        shape.setTranslateX((TILE_SIZE - TILE_SIZE*0.3125*2)/2);
        shape.setTranslateY((TILE_SIZE - TILE_SIZE*0.26*2)/2);
        getChildren().addAll(shape);
    }
}
