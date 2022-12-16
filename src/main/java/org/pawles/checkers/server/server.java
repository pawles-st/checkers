package org.pawles.checkers.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) { //try to make a new server at port 1234
            System.out.println("Server is running at port 1234");       //confirm that the server runs at given port
            var pool = Executors.newFixedThreadPool(200);
            while(true) {   // this has to be in while(true) because it has to be in loop, so it accepts all users
                GameBoard gameBoard = new GameBoard();                  //create new gameboard
                pool.execute(gameBoard.new Player(serverSocket.accept(), "White"));  //accept White Player
                pool.execute(gameBoard.new Player(serverSocket.accept(), "Black"));  //accept Black player
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    static class GameBoard {
        private int[][] board = new int[8][8];     //declare board
        Player currentPlayer;

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
                output = new PrintWriter(socket.getOutputStream());
            }
        }
    }
}
