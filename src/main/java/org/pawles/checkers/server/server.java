package org.pawles.checkers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) { //try to make a new server at port 1234
            System.out.println("Server is running at port 1234");       //confirm that the server runs at given port
            Socket firstClient;
            Socket secondClient;

            int boardSize = Integer.parseInt(args[0]);

            while (true) {   // this has to be in while(true) because it has to be in loop, so it accepts all users
                firstClient = serverSocket.accept();
                System.out.println("First player joined the game");

                secondClient = serverSocket.accept();
                System.out.println("Second player joined the game");


                Game g = new Game(firstClient, secondClient, boardSize); //create new game
                Thread gTh = new Thread(g);
                gTh.start(); // start new game


            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
