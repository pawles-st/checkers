package org.pawles.checkers.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;


/*
@deprecated
 */






public class GameBoard {
    private int[][] board = new int[8][8];     //declare board
    Player currentPlayer;
    Socket whitePlayer;
    Socket blackPlayer;

    GameBoard(Socket firstPlayer, Socket secondPlayer) {
        whitePlayer = firstPlayer;
        blackPlayer = secondPlayer;
    }
    public boolean enemyLost() {
        return (currentPlayer.opponent.pawns==0);
    }

    public synchronized void move() {
        if(currentPlayer.opponent == null) {
            throw new IllegalStateException("You don't have an opponent yet");
        }

        // TODO movement!!!

        currentPlayer = currentPlayer.opponent; // switch current player
    }

    class Player extends Thread {
        public int pawns;
        Player opponent;
        Socket socket;
        Scanner input;
        Writer output;
        String name;

        public Player(Socket socket, String name) {
            this.socket = socket;
            this.name = name;
            pawns = 12;
        }

        @Override
        public void run() {
            try {
                setup();
            } catch (IOException ioex) {
                System.out.println("Setup IOException");
            }
        }

        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            output.write(name); //outputs player color
        }
    }
}
