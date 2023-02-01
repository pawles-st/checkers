package org.pawles.checkers.serverSinglePlayer;

import org.pawles.checkers.db.Move;
import org.pawles.checkers.objects.AbstractPiece;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.server.*;

import java.net.Socket;
import java.util.List;
import java.util.Random;

public class Bot extends Player {
    public Bot(Socket opponent, Colour color, int boardSize) {
        this.socket = null;
        this.opponent = opponent;
        this.color = color;
        pieces = calcPieces(boardSize);
    }

    public MoveData generateMove() {
        int StartX = randomIntInRange(0, 8);
        int StartY = randomIntInRange(1, 8);
        boolean right;
        int EndX;
        int EndY = StartY - 1;

        if (StartX == 7) {
            right = false;
        } else if (StartX == 0) {
            right = true;
        } else {
            right = (randomIntInRange(0, 2) == 0);
        }

        if (right) {
            EndX = StartX + 1;
        } else {
            EndX = StartX - 1;
        }

        System.out.println("Im trying to go from: " + StartX + StartY + " to: " + EndX + EndY);

        return new MoveData(StartX, StartY, EndX, EndY);
    }

    public MoveData generateKill(List<List<AbstractPiece>> coordinates, Colour colour, int boardSize) {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                if (coordinates.get(y).get(x) != null) {
                    if (coordinates.get(y).get(x).getColour() == colour) {
                        if (MoveSimulator.tryToKill(coordinates, x, y, boardSize).isKillPossible()) {
                            KillData kd = MoveSimulator.tryToKill(coordinates, x, y, boardSize);

                            int endX;
                            int endY;

                            if (kd.isUp()) {
                                endY = y + 2;
                            } else {
                                endY = y - 2;
                            }

                            if (kd.isRight()) {
                                endX = x + 2;
                            } else {
                                endX = x - 2;
                            }

                            MoveData md = new MoveData(x, y, endX, endY);

                            System.out.println("Kill is possible: " + md);

                            return md;
                        }
                    }
                }
            }
        }
        return null;
    }

    public int randomIntInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
