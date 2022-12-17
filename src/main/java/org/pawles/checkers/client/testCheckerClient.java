package org.pawles.checkers.client;

import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public final class testCheckerClient {

    // fields for communication with the server

    private static final int PORT = 1234;
    private static Socket socket;
    private static Scanner socketIn;
    private static Writer socketOut;

    private testCheckerClient() { }

    private static void connect() throws IOException {

        // set socket and IO fields for communication

        try {
            socket = new Socket("localhost", PORT);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("connected succesfully");
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


            System.out.println("joined succesfully");
            // confirm the server agreed
            //if (!socketIn.nextBoolean()) {
            //    throw new RuntimeException("Couldn't join the game");
            //}
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

    private static void receiveColorFromServer(){
        String line;
        line = socketIn.nextLine();
        System.out.println(line);
    }

    public static void main(String[] args) {
        try {
            connect();
            receiveColorFromServer();
            joinGame();
            await();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
