package org.pawles.checkers.server;

import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Man;

import java.util.List;

/**
 * @author Szymon
 * @version 1.0
 */
public class MoveSimulator {
    /**
     * Simulate move to check if it's possible, and if what type it will be.
     *
     * @param goingUp - vertical direction of movement
     * @param goingRight - horizontal direction of movement
     * @param startX - starting X position
     * @param startY - starting Y position
     * @param moveLength - how far the piece is moving
     * @param coordinates - current board status
     * @return - NONE, NORMAL or KILL, depending what game move was recognized
     */
    public static MoveType simulate(final boolean goingUp, final boolean goingRight, final int startX, final int startY, final int moveLength, final List<List<AbstractPiece>> coordinates) {
        int verDirection = directionReader(goingUp);
        int horDirection = directionReader(goingRight);
        Colour playersColour = coordinates.get(startY).get(startX).getColour();
        int endX = startX + (moveLength * horDirection);
        int endY = startY + (moveLength * verDirection);
        AbstractPiece playersPiece = coordinates.get(startY).get(startX);

        if (playersPiece == null) { // if there isn't any pawn at start position
            return MoveType.NONE; // move cannot be done
        }

        if (coordinates.get(endY).get(endX) != null) { // if there is pawn at new coordinates
            return MoveType.NONE; // move cannot be done
        }

        if (playersPiece instanceof Man) {
            if (moveLength > 2) {
                return MoveType.NONE;
            }
            if (playersColour == Colour.WHITE && verDirection == -1 && moveLength == 1) {
                return MoveType.NONE;
            }
            if (playersColour == Colour.BLACK && verDirection == 1 && moveLength == 1) {
                return MoveType.NONE;
            }
        }


        for (int i = 1; i < moveLength; i++) {
            if (coordinates.get(startY + (i * verDirection)).get(startX + (i * horDirection)) != null) {
                if (i != moveLength - 1) {
                    return MoveType.NONE;
                } else if (coordinates.get(startY + (i * verDirection)).get(startX + (i * horDirection)).getColour() != playersColour) {
                    return MoveType.KILL;
                } else {
                    return MoveType.NONE;
                }
            }
        }

        // all tiles were empty, so move will be normal and can be done
        return MoveType.NORMAL;
    }

    /**
     * Look if given pawn can kill in any direction.
     * @param coordinates - current board status
     * @param xPos - starting X position of piece
     * @param yPos - starting Y position of piece
     * @param boardSize - size of the board, on which the game is being played
     * @return - true when given piece can kill, false if not
     */
    public static boolean tryToKill(final List<List<AbstractPiece>> coordinates, final int xPos, final int yPos, final int boardSize) {
        int topTiles = boardSize - 1 - yPos;
        int botTiles = yPos;
        int rightTiles = boardSize - 1 - xPos;
        int leftTiles = xPos;
        int[] directionTiles = new int[4];
        directionTiles[0] = Math.min(topTiles, rightTiles); // NE
        directionTiles[1] = Math.min(topTiles, leftTiles); // NW
        directionTiles[2] = Math.min(botTiles, rightTiles); // SE
        directionTiles[3] = Math.min(botTiles, leftTiles); // SW
        boolean up;
        boolean right;

        for (int i = 0; i < 4; i++) { // for every direction
            if (directionTiles[i] >= 2) { // if in given direction there are 2 or more tiles
                up = ((i / 2) == 0);
                right = ((i % 2) == 0);
                for (int j = 2; j <= directionTiles[i]; j++) {
                    if (simulate(up, right, xPos, yPos, j, coordinates) == MoveType.KILL) {
                        System.out.println("Kill in direction: " + up + "" + right + " tiles:" + j + " starting pos:" + xPos + "" + yPos);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Function made to convert 0;1 to -1;1.
     * @param direction if piece is moving in given direction
     * @return -1 or 1, depends on given bool
     */
    private static int directionReader(final boolean direction) {
        if (direction) {
            return 1;
        } else {
            return -1;
        }
    }
}
