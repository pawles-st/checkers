package org.pawles.checkers.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class Game implements Runnable {
    Socket whitePlayer;
    Scanner inputW;
    PrintWriter outputW;
    Socket blackPlayer;
    Scanner inputB;
    PrintWriter outputB;

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
        //TODO check if the move is possible to do

        return true;
    }

    private void movePawns(String move) {
        //TODO all the movement of pawns after the move
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
