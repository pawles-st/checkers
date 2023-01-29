package org.pawles.checkers.db;

public class Move {
    private int gameId;
    private int moveNr;

    public Move(int gameId, int moveNr) {
        this.gameId = gameId;
        this.moveNr = moveNr;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setMoveNr(int moveNr) {
        this.moveNr = moveNr;
    }

    public int getGameId() {
        return gameId;
    }

    public int getMoveNr() {
        return moveNr;
    }
}
