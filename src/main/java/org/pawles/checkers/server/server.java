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
            var pool = Executors.newFixedThreadPool(2);
            while(true) {   // this has to be in while(true) because it has to be in loop, so it accepts all users
                GameBoard gameBoard = new GameBoard();                  //create new gameboard
                pool.execute(gameBoard.new Player(serverSocket.accept(), "White"));  //accept White Player
                System.out.println("White player joined the game");
                pool.execute(gameBoard.new Player(serverSocket.accept(), "Black"));  //accept Black player
                System.out.println("Black player joined the game");
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
