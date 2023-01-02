package org.pawles.checkers.server;

public class MoveData {
    private int startX, startY, newX, newY;

    public int getNewX() {
        return newX;
    }
    public int getNewY() {
        return newY;
    }
    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }

    public MoveData(String line) {
        // read and store all data from string given from client app
        this.startX = Integer.parseInt(String.valueOf(line.charAt(0)));
        this.startY = Integer.parseInt(String.valueOf(line.charAt(1)));
        this.newX = Integer.parseInt(String.valueOf(line.charAt(3)));
        this.newY = Integer.parseInt(String.valueOf(line.charAt(4)));
    }
}
