package org.pawles.checkers.client;

import org.pawles.checkers.objects.Colour;
import org.pawles.checkers.objects.Square;

import java.io.PrintWriter;
import java.util.Scanner;

public class GameCommunicator {
    private final ClientController clientController;
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Scanner clientIn;
    private boolean myTurn = false;

    public GameCommunicator(final Colour colour, final Scanner socketIn, final PrintWriter socketOut) {

        // create ClientController object

        final ClientModel clientModel = new ClientModel(colour);
        final ClientView clientView = new ClientView();
        clientController = new ClientController(clientModel, clientView);

        // set sockets for communication with server

        this.socketIn = socketIn;
        this.socketOut = socketOut;

        // initialise stdin scanner

        clientIn = new Scanner(System.in);
    }

    public void start() {

        Square curr;
        Square dest;
        int currX;
        int currY;
        int destX;
        int destY;
        int turnCount = 1;

        // show board for the player

        clientController.updateView();

        // take turns while the game is in progress

        do {

            if (clientController.getColour() == Colour.BLACK || turnCount != 1) {
                // wait and read opponent's move

                System.out.println("Waiting for the opposing player...");
                final String opponentMove = socketIn.nextLine();
                currX = Integer.parseInt(String.valueOf(opponentMove.charAt(0)));
                currY = Integer.parseInt(String.valueOf(opponentMove.charAt(1)));
                curr = new Square(currX, currY);
                destX = Integer.parseInt(String.valueOf(opponentMove.charAt(3)));
                destY = Integer.parseInt(String.valueOf(opponentMove.charAt(4)));
                dest = new Square(destX, destY);
                clientController.movePiece(curr, dest);
            }

            // wait for player's turn

            final String msg = socketIn.nextLine();
            if ("your turn".equals(msg)) {
                myTurn = true;
            } else {
                throw new RuntimeException("Invalid message received");
            }

            // show opponent's move

            clientController.updateView();

            // single turn (can be composed of multiple moves)

            String verification = "";
            do {

                if ("incorrect".equals(verification)) {
                    System.out.println("Incorrect move, try again.");
                }

                // ask player for the move

                System.out.print("Move from: ");
                final String from = clientIn.nextLine();
                System.out.print("Move to: ");
                final String to = clientIn.nextLine(); //NOPMD - suppressed ShortVariable - name consistent with 'from'

                // convert move to Squares

                currX = Integer.parseInt(String.valueOf(from.charAt(0)));
                currY = Integer.parseInt(String.valueOf(from.charAt(1)));
                curr = new Square(currX, currY); // store squares somewhere to avoid constant creation???
                destX = Integer.parseInt(String.valueOf(to.charAt(0)));
                destY = Integer.parseInt(String.valueOf(to.charAt(1)));
                dest = new Square(destX, destY);

                // check

                if (!clientController.verifyMove(curr, dest)) {
                    clientController.updateView();
                    System.out.println("Incorrect move, try again.");
                    continue;
                }

                // sent the move to the server to verify

                final String move = from + ":" + to;
                socketOut.println(move);
                verification = socketIn.nextLine();
                clientController.updateView();
            } while (!"correct".equals(verification));

            // update the board

            clientController.movePiece(curr, dest);
            clientController.updateView();
            ++turnCount;

        } while (true); // while the game is in progress
    }
}
