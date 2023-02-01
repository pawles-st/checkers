package org.pawles.checkers.server;

public class KillData {
    private boolean killPossible;
    private boolean up;
    private boolean right;

    public boolean isKillPossible() {
        return killPossible;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isRight() {
        return right;
    }

    public KillData(boolean killPossible, boolean up, boolean right) {
        this.killPossible = killPossible;
        this.up = up;
        this.right = right;
    }

    public KillData(boolean killPossible) {
        this.killPossible = killPossible;
        this.up = false;
        this.right = false;
    }
}
