package org.pawles.checkers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(1234)) { //try to make a new server at port 1234
            System.out.println("Server is running 0000");

            Socket socket;

            while(true) {   // this has to be in while(true) because it has to be in loop, so it accepts all users
                socket = serverSocket.accept();
                System.out.println("New client connected successfully");
                new serverThread(socket).start();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
