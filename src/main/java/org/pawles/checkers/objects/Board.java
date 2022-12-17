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

    public void movePiece(int curr_x, int curr_y, int dest_x, int dest_y) {
        Piece piece = coordinates.get(curr_y).get(curr_x);
        coordinates.get(curr_y).set(curr_x, null);
        coordinates.get(dest_y).set(dest_x, piece);
    }
}
