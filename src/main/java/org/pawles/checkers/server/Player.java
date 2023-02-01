package org.pawles.checkers.server;

import org.pawles.checkers.objects.Colour;

import java.net.Socket;

/**
 * @author Szymon
 * @version 1.0
 */

public class Player {
    protected Colour color;

    /**
     * @return player's pieces color
     */
    public Colour getColor() {
        return color;
    }

    protected Socket socket;

    /**
     * @return player's socket
     */
    public Socket getSocket() {
        return socket;
    }

    protected Socket opponent;

    /**
     * @return opponent's socket
     */
    public Socket getOpponent() {
        return opponent;
    }

    protected int pieces;

    /**
     * @return amount of player's pieces
     */
    public int getPieces() {
        return pieces;
    }

    /**
     * Decrease player's piece number by 1.
     */
    public void removePieceFromBoard() {
        pieces = pieces - 1;
    }

    /**
     * @param socket player's socket
     * @param opponent opponent's socket
     * @param color player's color
     * @param boardSize size of the board, at which the game will be played
     */
    public Player(final Socket socket, final Socket opponent, final Colour color, final int boardSize) {
        this.socket = socket;
        this.opponent = opponent;
        this.color = color;
        pieces = calcPieces(boardSize);
    }

    /**
     * constructor fot bot
     */
    public Player(final Socket socket, final Colour color, final int boardSize) {
        this.socket = socket;
        this.opponent = null;
        this.color = color;
        pieces = calcPieces(boardSize);
    }

    public Player() {

    }

    /**
     * @param boardSize size of the board, at which the game will be played
     * @return ammount of pieces for each player
     */
    protected int calcPieces(final int boardSize) {
        return (boardSize / 2) * ((boardSize / 2) - 1);
    }
}
