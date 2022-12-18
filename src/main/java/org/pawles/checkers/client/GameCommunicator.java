package org.pawles.checkers.client;

import org.pawles.checkers.objects.Colour;

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
            if ("it's your turn".equals(msg)) {
                myTurn = true;
            }
            String verification;
            do {
                System.out.print("Move from: ");
                final String from = clientIn.nextLine();
                System.out.print("Move to: ");
                final String to = clientIn.nextLine();
                final String move = from + ":" + to;
                socketOut.println(move);
                verification = socketIn.nextLine();
                clientController.updateView();
            } while (!"correct".equals(verification));
        } while (true); // while the game is in progress
    }
}
