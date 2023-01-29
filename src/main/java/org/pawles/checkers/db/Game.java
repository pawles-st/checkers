package org.pawles.checkers.db;

import org.pawles.checkers.objects.Colour;

import java.util.Set;

public class Game {
    private int id;
    private int boardSize;
    private Colour winner;
    private Set<Move> moves;

    public Game(int boardSize, Colour winner) {
        this.boardSize = boardSize;
        this.winner = winner;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public  void setWinner(Colour winner) {
        this.winner = winner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Colour getWinner() {
        return winner;
    }

    public int getId() {
        return id;
    }

    public Set<Move> getMoves() {
        return moves;
    }
}
