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
import org.pawles.checkers.exceptions.UnknownPieceException;
import org.pawles.checkers.objects.*; //NOPMD - suppressed UnusedImports - these imports are used
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

import java.io.IOException;
import java.util.*;

public class CheckersApp extends Application { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded
    public static final String MAN_TYPE = "Man";
    public static final String KING_TYPE = "King";
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private static GameCommunicator gameCom;
    private transient Board board;
    private transient final Group tileGroup = new Group();
    private transient final Group pieceGroup = new Group();
    private transient final Map<Square, GraphicPiece> pieces = new HashMap<>(); //NOPMD - suppressed UseConcurrentHashMap - no multithreading needed

    private Parent createContent() {

        // create pane and set defaults
        final Pane root = new Pane();
        final ButtonFX button = new ButtonFX(gameCom);
        root.setPrefSize(WIDTH * TILE_SIZE, (HEIGHT+1) * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup, button);
        // read starting board status
        final List<List<AbstractPiece>> coordinates = board.getCoordinates();


        for(int y=7; y>=0; y--) {
            for(int x=7; x>=0; x--) {
                // create tile with right color (dark or light)
                final Colour colour = (x+y) % 2 == 0 ? Colour.BLACK : Colour.WHITE;
                final Tile tile = new Tile(colour, x, y); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - the loop exists for initialising objects
                // add created tile to tileGroup
                tileGroup.getChildren().add(tile);

                ///////////////////////////////////////////
                final Text text = new Text(x+""+y); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - the loop exists for initialising objects
                text.setFill(Color.PINK);
                text.relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);
                root.getChildren().add(text);
                ///////////////////////////////////////////

                // create GraphicPiece object
                GraphicPiece piece = null; //NOPMD - suppressed AvoidFinalLocalVariable
                // if square xy isn't null
                if(coordinates.get(y).get(x) != null) {
                    //check if it contains white or black piece, then create one
                    piece = new GraphicPiece(coordinates.get(y).get(x).getColour(), x, y, gameCom); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - the loop exists for initialising objects
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

    public void removePiece(final Square square) {
        pieceGroup.getChildren().remove(pieces.get(square));
    }

    public void movePiece(final Square curr, final Square dest) {
        final GraphicPiece piece = pieces.get(curr);
        piece.move(dest);
        pieces.remove(curr);
        pieces.put(dest, piece);
    }

    @Override
    public void start(final Stage primaryStage) {
        gameCom.setViewFX(this);
        // make board the same way as everywhere
        final BoardDirector director = new BoardDirector();
        director.setBoardBuilder(new BrazilianBoardBuilder());
        director.buildBoard();
        board = director.getBoard();
        // create scene using createContent function
        final Scene scene = new Scene(createContent());
        // set scene title and display it
        if(gameCom.getColour() == Colour.WHITE) {
            primaryStage.setTitle("Checkers - White");
            gameCom.waitForTurn();
        } else {
            primaryStage.setTitle("Checkers - Black");
            gameCom.waitForMove();
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args) {
        try {
            gameCom = ClientInitialiser.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        launch(); // call start() function
    }

    public void updateBoard(final Board board) {
        final List<List<AbstractPiece>> coordinates = board.getCoordinates();
        final Stack<GraphicPiece> changedWhiteMen = new Stack<>();
        final Stack<GraphicPiece> changedBlackMen = new Stack<>();
        final Stack<GraphicPiece> changedWhiteKings = new Stack<>();
        final Stack<GraphicPiece> changedBlackKings = new Stack<>();

        // push pieces that changed location onto the stack

        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                final GraphicPiece piece = pieces.get(SquareInstancer.getInstance(x, y));
                if (coordinates.get(y).get(x) == null && piece != null) {
                    if (piece.getColour() == Colour.WHITE && MAN_TYPE.equals(piece.getType())) {
                        changedWhiteMen.push(piece);
                    } else if (piece.getColour() == Colour.BLACK && MAN_TYPE.equals(piece.getType())) {
                        changedBlackMen.push(piece);
                    } else if (piece.getColour() == Colour.WHITE && KING_TYPE.equals(piece.getType())) {
                        changedWhiteKings.push(piece);
                    } else {
                        changedBlackKings.push(piece);
                    }
                    pieces.remove(SquareInstancer.getInstance(x, y));
                }
            }
        }

        // place the pieces back on new squares

        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                final AbstractPiece piece = coordinates.get(y).get(x);
                if (piece != null && pieces.get(SquareInstancer.getInstance(x, y)) == null) {
                    final GraphicPiece movedPiece; //NOPMD - suppressed AvoidFinalLocalVariable
                    if (piece.getColour() == Colour.WHITE) {
                        if (y == HEIGHT - 1) {
                            if (!changedWhiteKings.empty()) {
                                movedPiece = changedWhiteKings.pop();
                            } else {
                                movedPiece = changedWhiteMen.pop();
                                movedPiece.promote();
                            }
                        } else {
                            if (piece instanceof Man) {
                                movedPiece = changedWhiteMen.pop();
                            } else {
                                movedPiece = changedWhiteKings.pop();
                            }
                        }
                    } else if (piece.getColour() == Colour.BLACK) {
                        if (y == 0) {
                            if (!changedBlackKings.empty()) {
                                movedPiece = changedBlackKings.pop();
                            } else {
                                movedPiece = changedBlackMen.pop();
                                movedPiece.promote();
                            }
                        } else {
                            if (piece instanceof Man) {
                                movedPiece = changedBlackMen.pop();
                            } else {
                                movedPiece = changedBlackKings.pop();
                            }
                        }
                    } /*else if (piece.getColour() == Colour.WHITE && piece instanceof King) {
                        movedPiece = changedWhiteKings.pop();
                    } else if (piece.getColour() == Colour.BLACK && piece instanceof King) {
                        movedPiece = changedBlackKings.pop();
                    }*/ else {
                        throw new UnknownPieceException("Unknown piece found on the board while moving the pieces");
                    }
                    pieces.put(SquareInstancer.getInstance(x, y), movedPiece);
                    movedPiece.move(SquareInstancer.getInstance(x, y));
                }
            }
        }

        // remove extra pieces from the board

        while (!changedWhiteMen.empty()) {
            final GraphicPiece piece = changedWhiteMen.pop();
            pieceGroup.getChildren().remove(piece);
        }
        while (!changedBlackMen.empty()) {
            final GraphicPiece piece = changedBlackMen.pop();
            pieceGroup.getChildren().remove(piece);
        }
        while (!changedWhiteKings.empty()) {
            final GraphicPiece piece = changedWhiteKings.pop();
            pieceGroup.getChildren().remove(piece);
        }
        while (!changedBlackKings.empty()) {
            final GraphicPiece piece = changedBlackKings.pop();
            pieceGroup.getChildren().remove(piece);
        }
    }
}
