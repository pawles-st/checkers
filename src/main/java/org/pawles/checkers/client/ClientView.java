package org.pawles.checkers.client;

import org.pawles.checkers.exceptions.UnknownPieceException;
import org.pawles.checkers.objects.*; //NOPMD - suppressed UnusedImports - imports ARE indeed used

import java.io.PrintWriter;
import java.util.List;

/**
 * Commandline MVC view object
 * @author pawles
 * @version 1.0
 */
public class ClientView { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded, conflicting PMD warnings

    /** output stream for the view */
    private transient final PrintWriter out = new PrintWriter(System.out);

    private char convertTileToChar(final AbstractPiece piece) {  //NOPMD - suppressed CyclomaticComplexity - ifs are unavoidable
        char tileChar;
        if (piece == null) { // if tile is empty, return whitespace
            tileChar = ' ';
        } else if (piece.getColour() == Colour.WHITE && piece instanceof Man) { // if white man, return 'o'
            tileChar = 'o';
        } else if (piece.getColour() == Colour.BLACK && piece instanceof Man) { // if black man, return 'x'
            tileChar = 'x';
        } else if (piece.getColour() == Colour.WHITE && piece instanceof King) { // if white king, return 'O'
            tileChar = 'O';
        } else if (piece.getColour() == Colour.BLACK && piece instanceof King) { // if white king, return 'X'
            tileChar = 'X';
        } else {
            throw new UnknownPieceException("Unknown piece passed");
        }
        return tileChar;
    }

    /**
     * draws the board in commandline style
     * @param board current state of the board
     */
    public void drawBoard(final Board board) {

        final String hWall = "+-+-+-+-+-+-+-+-+"; //NOPMD - suppressed AvoidFinalLocalVariable - conflicting PMD warnings

        final List<List<AbstractPiece>> coordinates = board.getCoordinates(); //NOPMD - suppressed DataflowAnomalyAnalysis - no data anomaly here

        out.println(hWall);

        for (int y = 7; y >= 0; --y) {

            for (int x = 0; x < 8; ++x) {

                out.print('|');
                try {
                    out.print(convertTileToChar(coordinates.get(y).get(x))); //NOPMD - suppressed LawOfDemeter - 2D arraylist
                } catch (UnknownPieceException e) {
                    throw (UnknownPieceException) new UnknownPieceException("Unknown piece on the board").initCause(e); //NOPMD - suppressed AvoidInstantiatingObjectsInLoops - this is an exception creation
                }

            }

            out.println('|');

            out.println(hWall);
        }
    }
}
