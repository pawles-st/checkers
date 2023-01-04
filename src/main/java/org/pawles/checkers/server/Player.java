package org.pawles.checkers.server;

import org.pawles.checkers.objects.Colour;

import java.net.Socket;

public class Player {
    private Colour color;

    public Colour getColor() {
        return color;
    }

    private Colour opponentColor;

    public Colour getOpponentColor() {
        return opponentColor;
    }

    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    private Socket opponent;

    public Socket getOpponent() {
        return opponent;
    }

    public Player(Socket socket, Socket opponent, Colour color, Colour opponentColor) {
        this.socket = socket;
        this.opponent = opponent;
        this.color = color;
        this.opponentColor = opponentColor;
    }
}
