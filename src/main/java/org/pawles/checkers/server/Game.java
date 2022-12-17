package org.pawles.checkers.server;

import org.pawles.checkers.objects.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Game implements Runnable {
    Socket whitePlayer;
    Scanner inputW;
    PrintWriter outputW;
    Socket blackPlayer;
    Scanner inputB;
    PrintWriter outputB;

    Board board;

    int turn = 1;
    Game(Socket firstPlayer, Socket secondPlayer) {
        this.whitePlayer = firstPlayer;
        this.blackPlayer = secondPlayer;
    }

    @Override
    public void run() {
        try {
            setupWhite();
            setupBlack();

            String line;

            while(!gameLost()) {
                if(turn==1) {
                    // reading what move to do from whitePlayer
                    line = inputW.nextLine();

                    if(MoveIsCorrect(line)) {  // check if the move can be done
                        movePawns(line);       // do it
                        turn = 2;              // switch turn to the second player
                    } else {
                        outputW.println("Move is not correct, try other one");
                        continue;
                    }
                } else if (turn==2) {
                    // reading what move to do from blackPlayer
                    line = inputB.nextLine();

                    if(MoveIsCorrect(line)) {  // check if the move can be done
                        movePawns(line);       // do it
                        turn = 1;              // switch turn to the first player
                    } else {
                        outputW.println("Move is not correct, try other one");
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private boolean gameLost() {
        //TODO something like whitePlayer.pawns==0 || blackPlayer.pawns==0

        return false;
    }

    private boolean MoveIsCorrect(String move) {

        // assuming that move will loke like A2:B3 (which piece:where to move)
        //TODO Pawn = pawnAt(move.charAt(0), move.charAt(1))
        Square square = new Square(0,0);
        Piece pawn = new Man(square, Colour.WHITE);

        if(turn==1) { // it means, that the white pawn will move
            int pawnX = pawn.getSquare().getX();
            int pawnY = pawn.getSquare().getY();

            char newYchar = move.charAt(4); // get 5th character of "move"
            int newY = Integer.parseInt(String.valueOf(newYchar)); // convert this char to int

            char newXchar = move.charAt(3); // get 5th character of "move"
            int newX = Integer.parseInt(String.valueOf(newXchar)); // convert this char to int



        }
        return true;
    }

    private void movePawns(String move) {
        //TODO move the pawn, and delete dead ones
    }

    private void setupWhite() throws IOException {
        inputW = new Scanner(whitePlayer.getInputStream());
        outputW = new PrintWriter(whitePlayer.getOutputStream(), true);
        outputW.println("You are white player");
    }
    private void setupBlack() throws IOException {
        inputB = new Scanner(blackPlayer.getInputStream());
        outputB = new PrintWriter(blackPlayer.getOutputStream(), true);
        outputB.println("You are black player");
    }
}
