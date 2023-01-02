package org.pawles.checkers.server;

import org.pawles.checkers.objects.Piece;

public class MoveResult {
    private MoveType type;
    private Piece piece;

    public MoveType getType() {
        return type;
    }

    public MoveResult(MoveType type, Piece piece) {
        this.type = type;
        this.piece = piece;
    }

    public MoveResult(MoveType type){
        this(type, null);
    }
}
