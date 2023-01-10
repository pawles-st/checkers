package org.pawles.checkers.server;

import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Colour;

import java.util.List;

public class MoveSimulator {
    public static MoveType simulate(boolean goingUp, boolean goingRight, MoveData moveData, Colour playersColour, List<List<AbstractPiece>> coordinates) {
        int verDirection = directionReader(goingUp);
        int horDirection = directionReader(goingRight);
        int startX = moveData.getStartX();
        int startY = moveData.getStartY();
        int endX = moveData.getNewX();
        int endY = moveData.getNewY();
        int moveLength = (Math.abs(startX - endX));

        for (int i=1; i<moveLength; i++) {
            int x = startX+(i*horDirection);
            int y = startY+(i*verDirection);
            if(coordinates.get(startY+(i*verDirection)).get(startX+(i*horDirection)) == null) {
                System.out.println("Square: "+x+""+y+" is empty");
            } else {
                System.out.println("Square: "+x+""+y+" is not empty");
                if(i != moveLength-1) {
                    System.out.println("And it's not second to last tile i:"+i+" moveLength:"+moveLength);
                    return MoveType.NONE;
                } else if (coordinates.get(startY+(i*verDirection)).get(startX+(i*horDirection)).getColour() != playersColour) {
                    System.out.println("And it's second to last tile i:"+i+" moveLength:"+moveLength+" and it has opponents piece on it");
                    return MoveType.KILL;
                } else {
                    System.out.println("And it's second to last tile i:"+i+" moveLength:"+moveLength+" but it has yours piece on it");
                    return MoveType.NONE;
                }
            }
        }

        // all tiles were empty, so move will be normal and can be done
        return MoveType.NORMAL;
    }

    private static int directionReader(boolean direction) {
        if(direction) {
            return 1;
        } else {
            return -1;
        }
    }
}
