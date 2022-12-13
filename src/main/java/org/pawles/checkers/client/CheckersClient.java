package org.pawles.checkers.client;

import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public final class CheckersClient {

    private CheckersClient() { }

    // error messages

    private static final String cantConnectErrorMessage = "Can't connect to the server.";

    // fields for communication with the server

    private static final int PORT = 1234;
    private static Socket socket;
    private static Scanner socketIn;
    private static Writer socketOut;

    private static boolean connect() { // TODO: change type to void and throw appropriate exceptions

        // set socket and IO fields for communication

        try {
            socket = new Socket("localhost", PORT);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean joinGame() { // TODO: change type to void and throw appropriate exceptions
        try {

            // ask the server to join the game

            socketOut.write("join");

            // confirm the server agreed

            if (!socketIn.nextBoolean()) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void main(final String... args) {
        if (!connect() || !joinGame()) {
            System.err.println(cantConnectErrorMessage);
        }
    }
}
