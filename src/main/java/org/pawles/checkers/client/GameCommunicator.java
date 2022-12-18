package org.pawles.checkers.client;

import org.pawles.checkers.objects.Colour;

import java.io.PrintWriter;
import java.util.Scanner;

public class GameCommunicator {
    private final ClientController clientController;
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private boolean myTurn;

    public GameCommunicator(Colour colour, Scanner socketIn, PrintWriter socketOut) {

        // create ClientController object

        ClientModel clientModel = new ClientModel(colour);
        ClientView clientView = new ClientView();
        clientController = new ClientController(clientModel, clientView);

        // set sockets for communication with server

        this.socketIn = socketIn;
        this.socketOut = socketOut;
    }

    public void start() {
        // game interaction happens here
    }
}
