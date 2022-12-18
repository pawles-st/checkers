package org.pawles.checkers.client;

import org.pawles.checkers.objects.Colour;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public final class CheckersClient {

    // fields for communication with the server

    private static final int PORT = 1234;
    private static Socket socket;
    private static Scanner socketIn;
    private static PrintWriter socketOut;
    private static GameCommunicator gameCommunicator;
    private static Colour colour;

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

    private static void joinGame() { // TODO: replace RuntimeException
        // ask the server to join the game

        socketOut.println("join");

        // confirm the server agreed

        if (!socketIn.nextBoolean()) {
            throw new RuntimeException("Couldn't join the game");
        }
    }

    private static void await() { // TODO: replace RuntimeException
        System.out.println("Waiting for the game to begin...");
        String col = socketIn.nextLine();
        if ("White".equals(col)) {
            colour = Colour.WHITE;
        } else if ("Black".equals(col)) {
            colour = Colour.BLACK;
        } else {
            throw new RuntimeException("Game couldn't be started");
        }
    }

    private static void startGame(Colour colour) {
        gameCommunicator = new GameCommunicator(colour, socketIn, socketOut);
        gameCommunicator.start();
    }

    public static void main(final String... args) {
        try {
            connect();
            joinGame();
            await();
            startGame(colour);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
