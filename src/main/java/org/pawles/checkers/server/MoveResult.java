package org.pawles.checkers.server;

import org.pawles.checkers.objects.AbstractPiece;

public class MoveResult {
    private MoveType type;
    private AbstractPiece piece;

    public MoveType getType() {
        return type;
    }

    public MoveResult(MoveType type, AbstractPiece piece) {
        this.type = type;
        this.piece = piece;
    }

    public MoveResult(MoveType type){
        this(type, null);
    }
}
