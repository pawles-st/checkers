package org.pawles.checkers.checkers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Board;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

import java.util.List;

public class CheckersApp extends Application {
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    Board board;
    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    private Parent createContent() {
        // create pane and set defaults
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup);
        // read starting board status
        List<List<AbstractPiece>> coordinates = board.getCoordinates();


        for(int y=0; y<HEIGHT; y++) {
            for(int x=0; x<WIDTH; x++) {
                // create tile with right color (dark or light)
                Colour colour = (x+y) % 2 == 1 ? Colour.BLACK : Colour.WHITE;
                Tile tile = new Tile(colour, x, y);
                // add created tile to tileGroup
                tileGroup.getChildren().add(tile);

                // create GraphicPiece object
                GraphicPiece piece = null;
                // if square xy isn't null
                if(coordinates.get(y).get(x) != null) {
                    //check if it contains white or black piece, then create one
                    if (coordinates.get(y).get(x).getColour() == Colour.WHITE) {
                        piece = new GraphicPiece(Colour.WHITE, x, y);
                    } else if (coordinates.get(y).get(x).getColour() == Colour.BLACK) {
                        piece = new GraphicPiece(Colour.BLACK, x, y);
                    }
                }
                // if piece was created add it to this exact tile and pieceGroup
                if(piece != null) {
                    pieceGroup.getChildren().add(piece);
                }
            }
        }
        // return created board with pieces
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // make board the same way as everywhere
        BoardDirector director = new BoardDirector();
        director.setBoardBuilder(new BrazilianBoardBuilder());
        director.buildBoard();
        board = director.getBoard();
        // create scene using createContent function
        Scene scene = new Scene(createContent());
        // set scene title and display it
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(); // call start() function
    }
}
