package org.pawles.checkers.checkers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.pawles.checkers.client.ClientInitialiser;
import org.pawles.checkers.client.GameCommunicator;
import org.pawles.checkers.objects.*;
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckersApp extends Application {
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private static GameCommunicator gameCom;
    Board board;
    private final Group tileGroup = new Group();
    private final Group pieceGroup = new Group();
    private final Map<Square, GraphicPiece> pieces = new HashMap<>();

    private Parent createContent() {
        // create pane and set defaults
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup);
        // read starting board status
        List<List<AbstractPiece>> coordinates = board.getCoordinates();


        for(int y=7; y>=0; y--) {
            for(int x=7; x>=0; x--) {
                // create tile with right color (dark or light)
                Colour colour = (x+y) % 2 == 0 ? Colour.BLACK : Colour.WHITE;
                Tile tile = new Tile(colour, x, y);
                // add created tile to tileGroup
                tileGroup.getChildren().add(tile);

                ///////////////////////////////////////////
                Text text = new Text(x+""+y);
                text.setFill(Color.PINK);
                text.relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);
                root.getChildren().add(text);
                ///////////////////////////////////////////

                // create GraphicPiece object
                GraphicPiece piece = null;
                // if square xy isn't null
                if(coordinates.get(y).get(x) != null) {
                    //check if it contains white or black piece, then create one
                    piece = new GraphicPiece(coordinates.get(y).get(x).getColour(), x, y, gameCom);
                    pieces.put(SquareInstancer.getInstance(x, y), piece);
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
        if(gameCom.getColour() == Colour.WHITE) { // TODO: move this below primaryStage.show() later (helps testing if everything is ok)
            primaryStage.setTitle("Checkers - White");
            gameCom.waitForTurn();
        } else {
            primaryStage.setTitle("Checkers - Black");
            gameCom.waitForMove();
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            gameCom = ClientInitialiser.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        launch(); // call start() function
    }
}
