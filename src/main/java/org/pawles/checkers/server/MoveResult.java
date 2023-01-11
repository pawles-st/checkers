package org.pawles.checkers.server;

public class MoveResult {
    private final MoveType type;

    public MoveType getType() {
        return type;
    }

    public MoveResult(MoveType type) {
        this.type = type;
    }

}
