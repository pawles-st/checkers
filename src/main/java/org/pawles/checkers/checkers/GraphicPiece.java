package org.pawles.checkers.checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import org.pawles.checkers.client.GameCommunicator;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Square;
import org.pawles.checkers.objects.SquareInstancer;

import static org.pawles.checkers.checkers.CheckersApp.TILE_SIZE;

/**
 * Visual piece class
 * @author Szymon
 * @author pawles
 * @version 1.0
 */
public class GraphicPiece extends StackPane {

    /** piece's colour */
    private transient final Colour colour;

    /** previous x position of the piece */
    private transient int oldTileX;

    /** previous y position of the piece */
    private transient int oldTileY;

    /** type of the piece (Man/King) */
    private transient String type;

    /** piece's visual look */
    private transient final Ellipse shape;

    /**
     * constructs a GUI piece
     * @param colour piece's colour
     * @param x x initial position
     * @param y y initial position
     * @param communicator communicator for server messaging
     */
    public GraphicPiece(final Colour colour, final int x, final int y, final GameCommunicator communicator) { //NOPMD - suppressed ShortVariable - standard coordinate names
        super();
        this.colour = colour;
        type = "Man";
        relocate(x * TILE_SIZE, y*TILE_SIZE); //set right position
        // set some graphical values so it looks like checkers piece
        shape = new Ellipse(TILE_SIZE*0.3125, TILE_SIZE*0.26);
        shape.setFill(colour == Colour.WHITE ? Color.valueOf("#FFF") : Color.valueOf("#000"));
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
            final int newTileX = (int)e.getSceneX() / 100;
            final int newTileY = (int)e.getSceneY() / 100;
            if (communicator.sendMove(SquareInstancer.getInstance(oldTileX, oldTileY), SquareInstancer.getInstance(newTileX, newTileY))) {
                move(SquareInstancer.getInstance(newTileX, newTileY));
            } else {
                move(SquareInstancer.getInstance(oldTileX, oldTileY));
            }
        });
    }

    /**
     * moves the piece on the board
     * @param dest square to move to
     */
    public void move(final Square dest) {
        relocate(dest.getX() * TILE_SIZE, dest.getY() * TILE_SIZE);
    }

    public Colour getColour() {
        return colour;
    }

    /**
     * visually promotes the piece into a king
     */
    public void promote() {
        shape.setStrokeWidth(TILE_SIZE*0.06);
        shape.setStroke(Color.RED);
        type = "King";
    }

    public String getType() {
        return type;
    }
}
