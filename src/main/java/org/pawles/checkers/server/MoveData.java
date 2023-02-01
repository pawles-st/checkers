package org.pawles.checkers.server;

/**
 * @author Szymon
 * @version 1.0
 */
public class MoveData {
    private final int startX;
    private final int startY;
    private final int newX;
    private final int newY;

    /**
     * @return this move destination X
     */
    public int getNewX() {
        return newX;
    }
    /**
     * @return this move destination Y
     */
    public int getNewY() {
        return newY;
    }
    /**
     * @return this move starting X
     */
    public int getStartX() {
        return startX;
    }
    /**
     * @return this move starting Y
     */
    public int getStartY() {
        return startY;
    }

    public String toString() {
        return ("" + startX + startY + ":" + newX  + newY);
    }

    /**
     * @param line - string like XY:XY (from:where)
     */
    public MoveData(final String line) {
        // read and store all data from string given from client app
        this.startX = Integer.parseInt(String.valueOf(line.charAt(0)));
        this.startY = Integer.parseInt(String.valueOf(line.charAt(1)));
        this.newX = Integer.parseInt(String.valueOf(line.charAt(3)));
        this.newY = Integer.parseInt(String.valueOf(line.charAt(4)));
    }

    public MoveData(int startX, int startY, int newX, int newY) {
        this.startX = startX;
        this.startY = startY;
        this.newX = newX;
        this.newY = newY;
    }
}
