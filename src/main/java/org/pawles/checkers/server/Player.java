package org.pawles.checkers.server;

import org.pawles.checkers.objects.Colour;

import java.net.Socket;

/**
 * @author Szymon
 * @version 1.0
 */

public class Player {
    private final Colour color;

    /**
     * @return player's pieces color
     */
    public Colour getColor() {
        return color;
    }

    private final Socket socket;

    /**
     * @return player's socket
     */
    public Socket getSocket() {
        return socket;
    }

    private final Socket opponent;

    /**
     * @return opponent's socket
     */
    public Socket getOpponent() {
        return opponent;
    }

    private int Pieces;

    /**
     * @return amount of player's pieces
     */
    public int getPieces() {
        return Pieces;
    }

    /**
     * decrease player's piece number by 1
     */
    public void removePieceFromBoard() {
        Pieces = Pieces-1;
    }

    /**
     * @param socket player's socket
     * @param opponent opponent's socket
     * @param color player's color
     * @param boardSize size of the board, at which the game will be played
     */
    public Player(Socket socket, Socket opponent, Colour color, int boardSize) {
        this.socket = socket;
        this.opponent = opponent;
        this.color = color;
        Pieces = calcPieces(boardSize);
    }

    /**
     * @param boardSize size of the board, at which the game will be played
     * @return ammount of pieces for each player
     */
    private int calcPieces(int boardSize) {
        return (boardSize/2)*((boardSize/2)-1);
    }
}
