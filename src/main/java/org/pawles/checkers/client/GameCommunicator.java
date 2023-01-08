package org.pawles.checkers.client;

import org.pawles.checkers.checkers.CheckersApp;
import org.pawles.checkers.exceptions.WrongMessageException;
import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Square;
import org.pawles.checkers.objects.SquareInstancer;

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

    /** boolean informing whether it is the current player's turn */
    private transient boolean myTurn;

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
     * @param curr square to move from
     * @param dest square to move to
     * @return true if the move was correct; false otherwise
     */
    public boolean sendMove(final Square curr, final Square dest) {

        // verify if the move is allowed

        boolean verification;
        if (myTurn && clientController.verifyMove(curr, dest)) { // if player's turn and move is logically correct, send to server

            // send move to server

            final String move = Integer.toString(curr.getX()) + curr.getY() + ":" + dest.getX() + dest.getY();
            socketOut.println(move);

            // read response

            final String message = socketIn.nextLine();
            if (CORRECT_MESSAGE.equals(message)) { // if correct, apply move to the board and switch turns...
                clientController.movePiece(curr, dest);
                clientController.updateView();
                clientController.updateViewFX();
                myTurn = false;
                verification = true;
            } else if (INCORRECT_MESSAGE.equals(message)) { // ...otherwise move is incorrect
                verification = false;
            } else {
                throw new WrongMessageException("Unhandled message received: " + message);
            }
        } else {
            verification = false;
        }
        return verification;
    }

    /**
     * waits for the opponent's move from the server and applies it
     */
    public void waitForMove() {

        // if current player's turn, don't wait for opponent

        if (myTurn) {
            return;
        }
        final String move = socketIn.nextLine();        if (move.length() == 5 && move.charAt(2) == ':') { //NOPMD - suppressed LawOfDemeter - there is no LawOfDemeter here

            // parse the move

            final int currX = Integer.parseInt(String.valueOf(move.charAt(0)));
            final int currY = Integer.parseInt(String.valueOf(move.charAt(1)));
            final Square curr = SquareInstancer.getInstance(currX, currY);
            final int destX = Integer.parseInt(String.valueOf(move.charAt(3)));
            final int destY = Integer.parseInt(String.valueOf(move.charAt(4)));
            final Square dest = SquareInstancer.getInstance(destX, destY);

            // apply the move

            clientController.movePiece(curr, dest);
            clientController.updateView();
            clientController.updateViewFX();

            // wait for the player's turn

            waitForTurn();
        } else {
            throw new WrongMessageException("unhandled message received: " + move);
        }

    }

    /**
     * sets myTurn boolean once server informs about turn change
     */
    public void waitForTurn() {
        final String message = socketIn.nextLine();
        if ("your turn".equals(message)) {
            myTurn = true;
        } else {
            throw new WrongMessageException("Unhandled message received: " + message);
        }
    }

    /**
     * sets the JavaFX view for MVC controller
     * @param viewFX JavaFX view object
     */
    public void setViewFX(final CheckersApp viewFX) {
        clientController.setViewFX(viewFX);
    }
}
