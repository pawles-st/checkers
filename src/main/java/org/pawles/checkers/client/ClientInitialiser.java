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
 * @author Szymon
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

    /** size of the board */
    private static int boardSize;

    private ClientInitialiser() { }

    private static void connect() throws IOException {

        // set socket and IO fields for communication

        try {
            final Socket socket = new Socket("localhost", PORT); //NOPMD - suppressed CloseResource
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            throw (UnknownHostException) new UnknownHostException("Server doesn't exist").initCause(e);
        } catch (IOException e) {
            throw new IOException("couldn't establish a connection to the server", e);
        }
    }

    private static void await() {

        //reading boardSize
        final String size = socketIn.nextLine();
        boardSize = Integer.parseInt(size);

        // print the waiting message
        OUT.println("Waiting for the game to begin at "+boardSize+"x"+boardSize+" board");

        // when server returns the colour, finish waiting

        final String col = socketIn.nextLine();
        if ("White".equals(col)) {
            colour = Colour.WHITE;
        } else if ("Black".equals(col)) {
            colour = Colour.BLACK;
        } else {
            throw new CannotStartGameException("Couldn't receive the player's colour from the server");
        }
    }

    /**
     * connects to the server and initiates the client side
     */
    public static GameCommunicator init() throws IOException {
        try {
            connect();
            await();
            System.out.println("await complete");
        } catch (IOException e) {
            ERR.println(e.getMessage());
            throw new IOException(e);
        }
        return new GameCommunicator(colour, socketIn, socketOut, boardSize, true, null);
    }
}
