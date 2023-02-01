package org.pawles.checkers.serverSinglePlayer;

import org.pawles.checkers.server.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class serverSinglePlayer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) { //try to make a new server at port 1234
            System.out.println("Server is running at port 1234");       //confirm that the server runs at given port
            Socket soloClient;

            int boardSize = Integer.parseInt(args[0]);

            while (true) {   // this has to be in while(true) because it has to be in loop, so it accepts all users
                soloClient = serverSocket.accept();
                System.out.println("Player joined the game");

                Game g = new Game(soloClient, boardSize); //create new game
                Thread gTh = new Thread(g);
                gTh.start(); // start new game


            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
