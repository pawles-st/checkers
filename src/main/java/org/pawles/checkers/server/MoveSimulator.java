package org.pawles.checkers.server;

import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Man;

import java.util.List;

public class MoveSimulator {
    public static MoveType simulate(boolean goingUp, boolean goingRight, int startX, int startY, int moveLength, List<List<AbstractPiece>> coordinates) {
        int verDirection = directionReader(goingUp);
        int horDirection = directionReader(goingRight);
        Colour playersColour = coordinates.get(startY).get(startX).getColour();
        int endX = startX + (moveLength*horDirection);
        int endY = startY + (moveLength*verDirection);

        AbstractPiece playersPiece = coordinates.get(startY).get(startX);
        if(playersPiece == null) { // if there isn't any pawn at start position
            //System.out.println("Trying to move empty tile");
            return MoveType.NONE; // move cannot be done
        }

        if(coordinates.get(endY).get(endX) != null) { // if there is pawn at new coordinates
            //System.out.println("There is already a piece");
            return MoveType.NONE; // move cannot be done
        }

        //@TODO copy all statements from game to here

        if(playersPiece instanceof Man && moveLength > 2) {
            return MoveType.NONE;
        }


        for (int i=1; i<moveLength; i++) {
            int x = startX+(i*horDirection);
            int y = startY+(i*verDirection);
            if(coordinates.get(startY+(i*verDirection)).get(startX+(i*horDirection)) == null) {
                //System.out.println("Square: "+x+""+y+" is empty");
            } else {
                //System.out.println("Square: "+x+""+y+" is not empty");
                if(i != moveLength-1) {
                    //System.out.println("And it's not second to last tile i:"+i+" moveLength:"+moveLength);
                    return MoveType.NONE;
                } else if (coordinates.get(startY+(i*verDirection)).get(startX+(i*horDirection)).getColour() != playersColour) {
                    //System.out.println("And it's second to last tile i:"+i+" moveLength:"+moveLength+" and it has opponents piece on it");
                    return MoveType.KILL;
                } else {
                    //System.out.println("And it's second to last tile i:"+i+" moveLength:"+moveLength+" but it has yours piece on it");
                    return MoveType.NONE;
                }
            }
        }

        // all tiles were empty, so move will be normal and can be done
        return MoveType.NORMAL;
    }

    public static boolean tryToKill(List<List<AbstractPiece>> coordinates, int xPos, int yPos, int boardSize) {
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

        for(int i=0; i<4; i++) { // for every direction
            if( directionTiles[i] >= 2 ) {// if in given direction there are 2 or more tiles
                up = ((i/2) == 0);
                right = ((i%2) == 0);
                for (int j=2; j<=directionTiles[i]; j++) {
                    if (simulate(up, right,xPos, yPos, j, coordinates) == MoveType.KILL) {
                        System.out.println("Kill in direction: "+up+""+right+" tiles:"+j+" starting pos:"+xPos+""+yPos);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int directionReader(boolean direction) {
        if(direction) {
            return 1;
        } else {
            return -1;
        }
    }
}
