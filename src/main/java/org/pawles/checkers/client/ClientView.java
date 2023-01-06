package org.pawles.checkers.client;

import org.pawles.checkers.objects.Board;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.AbstractPiece;

import java.util.List;

// TODO: change to javaFX
// TODO: implement different view based on the colour of the player
// TODO: fix PMD errors
// TODO: document code
public class ClientView { //NOPMD - suppressed AtLeastOneConstructor - ctor unneeded, conflicting PMD warnings
    private char convertTileToChar(final AbstractPiece piece) {
        char tileChar;
        if (piece == null) { // if tile is empty, return whitespace
            tileChar = ' ';
        } else if (piece.getColour() == Colour.WHITE) { // if white piece, return 'O'
            tileChar = 'O';
        } else { // if black piece, return 'X'
            tileChar = 'X';
        }
        return tileChar;
    }
    
    public void drawBoard(final Board board) {

        final String hWall = "+-+-+-+-+-+-+-+-+"; //NOPMD - suppressed AvoidFinalLocalVariable - conflicting PMD warnings

        final List<List<AbstractPiece>> coordinates = board.getCoordinates();

        System.out.println(hWall);

        for (int y = 7; y >= 0; --y) {

            for (int x = 0; x < 8; ++x) {

                System.out.print('|');
                System.out.print(convertTileToChar(coordinates.get(y).get(x))); //NOPMD - suppressed LawOfDemeter - 2D arraylist

            }

            System.out.println('|');

            System.out.println(hWall);
        }
    }
}
