package org.pawles.checkers.objects;

public class Square { //NOPMD - suppressed DataClass - easier to read than providing coordinates alone
    private int x; //NOPMD - suppressed ShortVariable - x is a standard axis name
    private int y; //NOPMD - suppressed ShortVariable - y is a standard axis name

    public Square(final int x, final int y) { //NOPMD - suppressed ShortVariable - same as above
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(final int x) { //NOPMD - suppressed ShortVariable - same as above
        this.x = x;
    }

    public void setY(final int y) { //NOPMD - suppressed ShortVariable - same as above
        this.y = y;
    }
}
