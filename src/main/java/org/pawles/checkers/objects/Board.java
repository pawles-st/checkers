package org.pawles.checkers.objects;

import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<Piece>> coordinates;

    public Board() {

    }

    public ArrayList<ArrayList<Piece>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<ArrayList<Piece>> coordinates) {
        this.coordinates = coordinates;
    }

    public void setCoordinate(Square square, Piece piece) {
        coordinates.get(square.getY()).set(square.getX(), piece);
    }

    public void movePiece(Square curr, Square dest) {
        Piece piece = coordinates.get(curr.getY()).get(curr.getX());
        coordinates.get(curr.getY()).set(curr.getX(), null);
        coordinates.get(dest.getY()).set(dest.getX(), piece);
    }
}
