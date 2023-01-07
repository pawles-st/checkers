package org.pawles.checkers.client;

import org.pawles.checkers.exceptions.CannotStartGameException;
import org.pawles.checkers.objects.Colour;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Main class to run from the client side
 *
 * @author pawles
 * @version 1.0
 */
public final class ClientInitialiser {

    // fields for communication with the server

    /** number of the port to connect to */
    private static final int PORT = 1234;

    /** input stream for the server socket */
    private static Scanner socketIn;

    /** output stream for the server socket */
    private static PrintWriter socketOut;

    /** player colour for the game */
    private static Colour colour;

    // client message streams

    /** output stream for client messages */
    private static final PrintWriter OUT = new PrintWriter(System.out, true);

    /** error stream for client messages */
    private static final PrintWriter ERR = new PrintWriter(System.err);

    private ClientInitialiser() { }

    private static void connect() throws IOException {

        // set socket and IO fields for communication

        try {
            final Socket socket = new Socket("localhost", PORT);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            throw (UnknownHostException) new UnknownHostException("Server doesn't exist").initCause(e);
        } catch (IOException e) {
            throw new IOException("couldn't establish a connection to the server", e);
        }
    }

    private static void await() {

        // print the waiting message

        OUT.println("Waiting for the game to begin...");

        // when server returns the colour, finish waiting

        final String col = socketIn.nextLine();
        if ("White".equals(col)) {
            OUT.println("received white color");
            colour = Colour.WHITE;
        } else if ("Black".equals(col)) {
            OUT.println("received black color");
            colour = Colour.BLACK;
        } else {
            throw new CannotStartGameException("Couldn't receive the player's colour from the server");
        }
    }

    /**
     * connects to the server and initiates tho client side
     */
    public static GameCommunicator init() throws IOException {
        try {
            connect();
            await();
        } catch (IOException e) {
            ERR.println(e.getMessage());
            throw new IOException(e);
        }
        return new GameCommunicator(colour, socketIn, socketOut);
    }
}
