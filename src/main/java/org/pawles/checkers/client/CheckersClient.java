package org.pawles.checkers.client;

import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public final class CheckersClient {

    // fields for communication with the server

    private static final int PORT = 1234;
    private static Socket socket;
    private static Scanner socketIn;
    private static Writer socketOut;

    private CheckersClient() { }

    private static void connect() throws IOException {

        // set socket and IO fields for communication

        try {
            socket = new Socket("localhost", PORT);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            throw new UnknownHostException("Couldn't find host");
        } catch (IOException e) {
            throw new IOException("Couldn't establish a connection with the server");
        }
    }

    private static void joinGame() throws IOException { // TODO: replace RuntimeException
        try {

            // ask the server to join the game

            socketOut.write("join");

            // confirm the server agreed

            if (!socketIn.nextBoolean()) {
                throw new RuntimeException("Couldn't join the game");
            }
        } catch (IOException e) {
            throw new IOException("Couldn't ask the server to join the game");
        }
    }

    private static void await() { // TODO: replace RuntimeException
        System.out.println("Waiting for the game to begin...");
        if (!socketIn.nextBoolean()) {
            throw new RuntimeException("Game couldn't be started");
        } else {
            startGame();
        }
    }

    private static void startGame() {
        // TODO: implement game interaction
    }

    public static void main(final String... args) {
        try {
            connect();
            joinGame();
            await();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
