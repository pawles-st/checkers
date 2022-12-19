package org.pawles.checkers.client;

import org.pawles.checkers.objects.Board;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Piece;

import java.util.ArrayList;

public class ClientView { // TODO: implement different view based on the colour of the player
    
    private char convertTileToChar(Piece piece) {
        if (piece == null) { // if tile is empty, return whitespace
            return ' ';
        } else if (piece.getColour() == Colour.WHITE) { // if white piece, return 'O'
            return 'O';
        } else { // if black piece, return 'X'
            return 'X';
        }
    }
    
    public void drawBoard(Board board) {

        final String hWall = "+-+-+-+-+-+-+-+-+";

        ArrayList<ArrayList<Piece>> coordinates = board.getCoordinates();

        System.out.println(hWall);

        for (int y = 7; y >= 0; --y) {

            for (int x = 0; x < 8; ++x) {

                System.out.print('|');
                System.out.print(convertTileToChar(coordinates.get(y).get(x)));

            }

            System.out.println('|');

            System.out.println(hWall);
        }
    }
}
