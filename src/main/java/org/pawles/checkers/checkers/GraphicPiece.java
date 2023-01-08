package org.pawles.checkers.checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import org.pawles.checkers.client.GameCommunicator;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Square;
import org.pawles.checkers.objects.SquareInstancer;

import static org.pawles.checkers.checkers.CheckersApp.TILE_SIZE;

public class GraphicPiece extends StackPane {
    Colour colour;
    int posX, posY;
    int oldTileX, oldTileY;
    int newTileX, newTileY;
    String type;
    final Ellipse shape;
    public GraphicPiece(Colour color, int x, int y, GameCommunicator communicator) {
        this.colour = color;
        // store info on which tile the pawn is
        posX = x;
        posY = y;
        type = "Man";
        relocate(x * TILE_SIZE, y*TILE_SIZE); //set right position
        // set some graphical values so it looks like checkers piece
        shape = new Ellipse(TILE_SIZE*0.3125, TILE_SIZE*0.26);
        shape.setFill(color == Colour.WHITE ? Color.valueOf("#FFF") : Color.valueOf("#000"));
        shape.setStroke(Color.GRAY);
        shape.setStrokeWidth(TILE_SIZE*0.03);
        shape.setTranslateX((TILE_SIZE - TILE_SIZE*0.3125*2)/2);
        shape.setTranslateY((TILE_SIZE - TILE_SIZE*0.26*2)/2);
        // add this shape to list of graphicPieces
        getChildren().addAll(shape);

        setOnMousePressed( e-> {
            oldTileX = (int)e.getSceneX() / 100;
            oldTileY = (int)e.getSceneY() / 100;
        });

        setOnMouseDragged( e-> relocate(e.getSceneX()-TILE_SIZE*0.5, e.getSceneY()-TILE_SIZE*0.5));

        setOnMouseReleased( e-> {
            newTileX = (int)e.getSceneX() / 100;
            newTileY = (int)e.getSceneY() / 100;
            String str1 = Integer.toString(oldTileX) + (oldTileY) + ":" + (newTileX) + (newTileY);
            System.out.println(str1);
            if (communicator.sendMove(SquareInstancer.getInstance(oldTileX, oldTileY), SquareInstancer.getInstance(newTileX, newTileY))) {
                System.out.println("Old position: "+oldTileX+""+oldTileY+". New position: "+newTileX+""+newTileY);
                move(SquareInstancer.getInstance(newTileX, newTileY));
                //communicator.waitForMove();
            } else {
                System.out.println("bad");
                move(SquareInstancer.getInstance(oldTileX, oldTileY));
            }
        });
    }

    public void move(Square dest) {
        oldTileX = dest.getX();
        oldTileY = dest.getY();
        relocate(dest.getX() * TILE_SIZE, dest.getY() * TILE_SIZE);
    }

    public Colour getColour() {
        return colour;
    }

    public void promote() {
        shape.setStrokeWidth(TILE_SIZE*0.06);
        shape.setStroke(Color.RED);
        type = "King";
    }

    public String getType() {
        return type;
    }
}
