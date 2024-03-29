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
import org.pawles.checkers.objects.*; //NOPMD - suppressed UnusedImports - these imports are used
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * main JavaFX client class
 * @author Szymon
 * @author pawles
 * @version 1.0
 */
public class CheckersApp extends Application { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded

    /** FX size of a single tile */
    public static final int TILE_SIZE = 60;

    /** communicator object for interacting with the server */
    private static GameCommunicator gameCom;
    private static boolean online;

    /** game board */
    private transient Board board;

    /** group composed of all the FX tiles */
    private transient final Group tileGroup = new Group();

    /** group composed of all the FX */
    private transient final Group pieceGroup = new Group();

    /** pieces on the board */
    private transient final Map<Square, GraphicPiece> pieces = new HashMap<>(); //NOPMD - suppressed UseConcurrentHashMap - no multithreading needed

    /** size of the board */
    private transient int boardSize;

    private static int gameId;

    private Parent createContent() {

        // create pane and set defaults
        final Pane root = new Pane();
        final ButtonFX button = new ButtonFX(gameCom, boardSize, online);
        root.setPrefSize(boardSize * TILE_SIZE, (boardSize+1) * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup, button); //NOPMD - suppressed LawOfDemeter
        // read starting board status
        final List<List<AbstractPiece>> coordinates = board.getCoordinates();


        for(int y=boardSize-1; y>=0; y--) {
            for(int x=boardSize-1; x>=0; x--) {
                // create tile with right color (dark or light)
                final Colour colour = (x+y) % 2 == 0 ? Colour.BLACK : Colour.WHITE;
                final Tile tile = new Tile(colour, x, y); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - the loop exists for initialising objects
                // add created tile to tileGroup
                tileGroup.getChildren().add(tile); //NOPMD - suppressed LawOfDemeter

                ///////////////////////////////////////////
                final Text text = new Text(x+""+y); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - the loop exists for initialising objects
                text.setFill(Color.PINK);
                text.relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);
                root.getChildren().add(text); //NOPMD - suppressed LawOfDemeter
                ///////////////////////////////////////////

                // create GraphicPiece object
                GraphicPiece piece = null; //NOPMD - suppressed AvoidFinalLocalVariable
                // if square xy isn't null
                if(coordinates.get(y).get(x) != null) { //NOPMD - suppressed LawOfDemeter
                    //check if it contains white or black piece, then create one
                    piece = new GraphicPiece(coordinates.get(y).get(x).getColour(), x, y, gameCom); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - the loop exists for initialising objects
                    pieces.put(SquareInstancer.getInstance(x, y), piece);
                }
                // if piece was created add it to this exact tile and pieceGroup
                if(piece != null) {
                    pieceGroup.getChildren().add(piece); //NOPMD - suppressed LawOfDemeter
                }
            }
        }
        // return created board with pieces
        return root;
    }

    /**
     * removes a single piece
     * @param piece piece to remove
     */
    public void removePiece(final GraphicPiece piece) {
        pieceGroup.getChildren().remove(piece); //NOPMD - suppressed LawOfDemeter
    }

    /**
     * moves a single piece between tiles
     * @param curr square to move from
     * @param dest square to move to
     */
    public void movePiece(final Square curr, final Square dest) {
        final GraphicPiece piece = pieces.get(curr);
        piece.move(dest); //NOPMD - suppressed LawOfDemeter
        pieces.remove(curr);
        pieces.put(dest, piece);
    }

    @Override
    public void start(final Stage primaryStage) {
        // make board the same way as everywhere
        boardSize = gameCom.getBoardSize();
        gameCom.setViewFX(new ClientViewFX(this, pieces, boardSize));
        final BoardDirector director = new BoardDirector();
        director.setBoardBuilder(new BrazilianBoardBuilder());
        director.buildBoard(boardSize);
        board = director.getBoard();
        // create scene using createContent function
        final Scene scene = new Scene(createContent());
        // set scene title and display it
        if(gameCom.getColour() == Colour.WHITE) {
            primaryStage.setTitle("Checkers - White");
            if (online) {
                gameCom.waitForMove();
            }
        } else {
            primaryStage.setTitle("Checkers - Black");
            if (online) {
                gameCom.waitForMove();
            }
        }
        primaryStage.setScene(scene);
        System.out.println("showing");
        primaryStage.show();
        if (!online) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/checkers", "checkers_admin", "admin");
                Statement statement = connection.createStatement();
                ResultSet resultSetMoves = statement.executeQuery("SELECT move_value FROM moves WHERE game_id = " + gameId + " ORDER BY move_nr ASC;");
                //while (resultSetMoves.next()) {
                    //final String move = resultSetMoves.getString("move_value");
                    //MoveData moveData = new MoveData(move);
                    //gameCom.sendMove(SquareInstancer.getInstance(moveData.getStartX(), moveData.getStartY()),
                            //SquareInstancer.getInstance(moveData.getNewX(), moveData.getNewY()));
                    Thread.sleep(1000);
                //}
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * starts the client program
     * @param args entry arguments (leave empty)
     * @throws IOException if couldn't establish a connection with the server
     */
    public static void main(final String[] args) throws IOException {
        if (args.length == 0) { // player wants to play a game
            try {
                gameCom = ClientInitialiser.init();
                online = true;
            } catch (IOException e) {
                throw new IOException("", e);
            }
            launch(); // call start() function
        } else { // player wants to see a replay
            gameId = Integer.parseInt(args[0]);
            try {
                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/checkers", "checkers_admin", "admin");
                Statement statement = connection.createStatement();
                ResultSet resultSetBoard = statement.executeQuery("SELECT board_size FROM games WHERE id = " + gameId + ";");
                resultSetBoard.next();
                final int boardSize = resultSetBoard.getInt("board_size");
                online = false;
                ResultSet resultSetMoves = statement.executeQuery("SELECT move_value FROM moves WHERE game_id = " + gameId + " ORDER BY move_nr ASC;");
                gameCom = new GameCommunicator(Colour.WHITE, null, null, boardSize, online, resultSetMoves);
                launch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
