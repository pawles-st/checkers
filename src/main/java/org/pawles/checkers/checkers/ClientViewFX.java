package org.pawles.checkers.checkers;

import org.pawles.checkers.exceptions.UnknownPieceException;
import org.pawles.checkers.objects.*; //NOPMD - suppressed UnusedImports

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * MVC view class for JavaFX client side
 * @author pawles
 * @version 1.0
 */
public class ClientViewFX { //NOPMD - suppressed StdCyclomaticComplexity

    /** man type string */
    public static final String MAN_TYPE = "Man";

    /** king type string */
    public static final String KING_TYPE = "King";

    /** main JavaFX application object */
    private transient final CheckersApp app;

    /** pieces currently on the board */
    private transient final Map<Square, GraphicPiece> pieces;

    /** size of the board */
    private transient final int boardSize;

    /** stack for white men that moved since last update */
    private transient final Stack<GraphicPiece> changedWhiteMen = new Stack<>();

    /** stack for black men that moved since last update */
    private transient final Stack<GraphicPiece> changedBlackMen = new Stack<>();

    /** stack for white kings that moved since last update */
    private transient final Stack<GraphicPiece> changedWhiteKings = new Stack<>();

    /** stack for black kings that moved since last update */
    private transient final Stack<GraphicPiece> changedBlackKings = new Stack<>();

    /**
     * constructs the FX view object
     * @param app main javaFX client app
     * @param pieces map square : pieces reflecting the board
     * @param boardSize size of the board
     */
    public ClientViewFX(final CheckersApp app, final Map<Square, GraphicPiece> pieces, final int boardSize) {
        this.app = app;
        this.pieces = pieces;
        this.boardSize = boardSize;
    }

    /**
     * updates the FX view of the game board
     * @param board current state of the board
     */
    public void updateView(final Board board) {
        takePieces(board);
        placePieces(board);
        removePieces();
    }

    // push pieces that changed location onto the stack
    private void takePieces(final Board board) { //NOPMD - suppressed CognitiveComplexity
        final List<List<AbstractPiece>> coordinates = board.getCoordinates(); //NOPMD - suppressed DataflowAnomalyAnalysis
        for (int y = 0; y < boardSize; ++y) {
            for (int x = 0; x < boardSize; ++x) {
                final GraphicPiece piece = pieces.get(SquareInstancer.getInstance(x, y));
                if (coordinates.get(y).get(x) == null && piece != null) { //NOPMD - suppressed LawOfDemeter - 2D Array
                    if (piece.getColour() == Colour.WHITE && MAN_TYPE.equals(piece.getType())) { //NOPMD - suppressed LawOfDemeter - 2D Array
                        changedWhiteMen.push(piece);
                    } else if (piece.getColour() == Colour.BLACK && MAN_TYPE.equals(piece.getType())) { //NOPMD - suppressed LawOfDemeter - 2D Array
                        changedBlackMen.push(piece);
                    } else if (piece.getColour() == Colour.WHITE && KING_TYPE.equals(piece.getType())) { //NOPMD - suppressed LawOfDemeter - 2D Array
                        changedWhiteKings.push(piece);
                    } else {
                        changedBlackKings.push(piece);
                    }
                    pieces.remove(SquareInstancer.getInstance(x, y));
                }
            }
        }
    }

    // place the pieces back on new squares
    private void placePieces(final Board board) { //NOPMD - suppressed CognitiveComplexity
        for (int y = 0; y < boardSize; ++y) {
            for (int x = 0; x < boardSize; ++x) {
                final AbstractPiece piece = board.getCoordinates().get(y).get(x); //NOPMD - suppressed LawOfDemeter - 2D Array
                if (piece != null && pieces.get(SquareInstancer.getInstance(x, y)) == null) {
                    final GraphicPiece movedPiece; //NOPMD - suppressed AvoidFinalLocalVariable
                    if (piece.getColour() == Colour.WHITE) { //NOPMD - suppressed LawOfDemeter - Array
                        if (y == boardSize - 1) {
                            if (changedWhiteKings.empty()) {
                                movedPiece = changedWhiteMen.pop();
                                movedPiece.promote(); //NOPMD - suppressed LawOfDemeter - Array
                            } else {
                                movedPiece = changedWhiteKings.pop();
                            }
                        } else {
                            if (piece instanceof Man) {
                                movedPiece = changedWhiteMen.pop();
                            } else {
                                movedPiece = changedWhiteKings.pop();
                            }
                        }
                    } else if (piece.getColour() == Colour.BLACK) { //NOPMD - suppressed LawOfDemeter - Array
                        if (y == 0) {
                            if (changedBlackKings.empty()) {
                                movedPiece = changedBlackMen.pop();
                                movedPiece.promote(); //NOPMD - suppressed LawOfDemeter - Array
                            } else {
                                movedPiece = changedBlackKings.pop();
                            }
                        } else {
                            if (piece instanceof Man) {
                                movedPiece = changedBlackMen.pop();
                            } else {
                                movedPiece = changedBlackKings.pop();
                            }
                        }
                    } else {
                        throw new UnknownPieceException("Unknown piece found on the board while moving the pieces");
                    }
                    pieces.put(SquareInstancer.getInstance(x, y), movedPiece);
                    movedPiece.move(SquareInstancer.getInstance(x, y)); //NOPMD - suppressed LawOfDemeter - Array
                }
            }
        }
    }

    // remove extra pieces from the board
    private void removePieces() {
        while (!changedWhiteMen.empty()) {
            final GraphicPiece piece = changedWhiteMen.pop();
            app.removePiece(piece);
        }
        while (!changedBlackMen.empty()) {
            final GraphicPiece piece = changedBlackMen.pop();
            app.removePiece(piece);
        }
        while (!changedWhiteKings.empty()) {
            final GraphicPiece piece = changedWhiteKings.pop();
            app.removePiece(piece);
        }
        while (!changedBlackKings.empty()) {
            final GraphicPiece piece = changedBlackKings.pop();
            app.removePiece(piece);
        }
    }
}
