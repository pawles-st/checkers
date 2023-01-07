package org.pawles.checkers.client;

import org.pawles.checkers.exceptions.WrongMessageException;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Square;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Client communicator with the server that also applies moves
 * @author pawles
 * @version 1.0
 */
public class GameCommunicator {

    /** MVC controller object */
    private transient final ClientController clientController;

    /** input stream for server communication */
    private transient final Scanner socketIn;

    /** output stream for server communication */
    private transient final PrintWriter socketOut;

    /** string representing the correct message passed from the server side */
    private static final String CORRECT_MESSAGE = "correct";

    /** string representing the correct message passed from the server side */
    private static final String INCORRECT_MESSAGE = "incorrect";

    private boolean myTurn = false;

    public Colour getColour() { //NOPMD - suppressed CommentRequired - this is a simple getter
        return clientController.getColour();
    }

    /**
     * initialises the server communicator object
     * @param colour player's colour
     * @param socketIn input stream for server communication
     * @param socketOut output stream for server communication
     */
    public GameCommunicator(final Colour colour, final Scanner socketIn, final PrintWriter socketOut) {

        // create ClientController object

        final ClientModel clientModel = new ClientModel(colour);
        final ClientView clientView = new ClientView();
        clientController = new ClientController(clientModel, clientView);

        // set sockets for communication with server

        this.socketIn = socketIn;
        this.socketOut = socketOut;
    }

    /**
     * sends the move to the server and applies if it is correct
     * @param move move to send to the server [oldX][oldY]:[newX][newY]
     * @return true if the move was correct; false otherwise
     */
    public boolean sendMove(final String move) {

        // parse the move

        final int currX = Integer.parseInt(String.valueOf(move.charAt(0)));
        final int currY = Integer.parseInt(String.valueOf(move.charAt(1)));
        final Square curr = new Square(currX, currY);
        final int destX = Integer.parseInt(String.valueOf(move.charAt(3)));
        final int destY = Integer.parseInt(String.valueOf(move.charAt(4)));
        final Square dest = new Square(destX, destY);

        // verify

        boolean verification;
        if (myTurn && clientController.verifyMove(curr, dest)) {
            socketOut.println(move);
            final String message = socketIn.nextLine();
            if (CORRECT_MESSAGE.equals(message)) {
                clientController.movePiece(curr, dest);
                myTurn = false;
                verification = true;
            } else if (INCORRECT_MESSAGE.equals(message)) {
                verification = false;
            } else {
                throw new WrongMessageException("unhandled message received: " + message);
            }
        } else {
            verification = false;
        }
        return verification;
    }

    public void waitForMove() {
        final String move = socketIn.nextLine();
        if (move.length() == 5 && move.charAt(2) == ':') {
            final int currX = Integer.parseInt(String.valueOf(move.charAt(0)));
            final int currY = Integer.parseInt(String.valueOf(move.charAt(1)));
            final Square curr = new Square(currX, currY);
            final int destX = Integer.parseInt(String.valueOf(move.charAt(3)));
            final int destY = Integer.parseInt(String.valueOf(move.charAt(4)));
            final Square dest = new Square(destX, destY);
            clientController.movePiece(curr, dest);
            waitForTurn();
        } else {
            throw new WrongMessageException("unhandled message received: " + move);
        }

    }

    public void waitForTurn() {
        final String message = socketIn.nextLine();
        if ("your turn".equals(message)) {
            myTurn = true;
        } else {
            throw new WrongMessageException("unhandled message received: " + message);
        }
    }
}
