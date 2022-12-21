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

    public GameCommunicator(Colour colour, Scanner socketIn, PrintWriter socketOut) {

        // create ClientController object

        ClientModel clientModel = new ClientModel(colour);
        ClientView clientView = new ClientView();
        clientController = new ClientController(clientModel, clientView);

        // set sockets for communication with server

        this.socketIn = socketIn;
        this.socketOut = socketOut;

        // initialise stdin scanner

        clientIn = new Scanner(System.in);
    }

    public void start() {
        clientController.updateView();
        do {
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
            Square curr;
            Square dest;
            do {

                if ("incorrect".equals(verification)) {
                    System.out.println("Incorrect move, try again.");
                }

                // ask player for the move

                System.out.print("Move from: ");
                final String from = clientIn.nextLine();
                System.out.print("Move to: ");
                final String to = clientIn.nextLine();

                // convert move to Squares

                int curr_x = Integer.parseInt(String.valueOf(from.charAt(0)));
                int curr_y = Integer.parseInt(String.valueOf(from.charAt(1)));
                curr = new Square(curr_x, curr_y); // store squares somewhere to avoid constant creation???
                int dest_x = Integer.parseInt(String.valueOf(to.charAt(0)));
                int dest_y = Integer.parseInt(String.valueOf(to.charAt(1)));
                dest = new Square(dest_x, dest_y);

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
        } while (true); // while the game is in progress
    }
}
