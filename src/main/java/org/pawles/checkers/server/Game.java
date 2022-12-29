package org.pawles.checkers.server;

import org.pawles.checkers.client.ClientView;
import org.pawles.checkers.objects.*;
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

import java.io.IOException;
import java.io.PrintWriter;
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

    ClientView cView;

    int turn = 1;
    Game(Socket firstPlayer, Socket secondPlayer) {
        this.whitePlayer = firstPlayer;
        this.blackPlayer = secondPlayer;

        //TODO fix Index 0 out of bounds, BrazilianBoardBuilder
        BoardDirector director = new BoardDirector();
        director.setBoardBuilder(new BrazilianBoardBuilder());
        director.buildBoard();
        board = director.getBoard();

        cView = new ClientView();

    }

    @Override
    public void run() {
        try {
            setupWhite();
            setupBlack();

            String line;

            while(!gameLost()) {
                if(turn==1) {
                    outputW.println("your turn");
                    do {
                        System.out.println("Waiting for whitePlayer input");
                        // reading what move to do from whitePlayer
                        line = inputW.nextLine();
                        // outputing it on server
                        System.out.println("White input: "+line);

                        if(MoveIsCorrect(line)) {  // check if the move can be done
                            movePawns(line);       // do it
                            turn = 2;              // switch turn to the second player
                            outputB.println(line); // send the move to the second player
                        } else {
                            outputW.println("incorrect"); // send info to client, that his move cannot be done
                        }
                    } while (turn == 1);
                    outputW.println("correct"); // send info to client, that the move is correct and will be done
                } else if (turn==2) {
                    outputB.println("your turn");
                    do {
                        System.out.println("Waiting for blackPlayer input");
                        // reading what move to do from blackPlayer
                        line = inputB.nextLine();
                        // outputing it on server
                        System.out.println("Black input: " + line);

                        if (MoveIsCorrect(line)) {  // check if the move can be done
                            movePawns(line);       // do it
                            turn = 1;              // switch turn to the first player
                            outputW.println(line); // send the move to the second player
                        } else {
                            outputB.println("incorrect"); // send info to client, that his move cannot be done
                        }
                    } while (turn == 2);
                    outputB.println("correct"); // send info to client, that the move is correct and will be done
                }
                cView.drawBoard(board); // after every move draw current board status on server terminal
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
        ArrayList<ArrayList<Piece>> coordinates = board.getCoordinates();       // get current board status
        // assuming that move will look like A2:B3 (which piece:where to move)
        // read all data about user move

        int startX = Integer.parseInt(String.valueOf(move.charAt(0)));
        int startY = Integer.parseInt(String.valueOf(move.charAt(1)));

        int newX = Integer.parseInt(String.valueOf(move.charAt(3)));
        int newY = Integer.parseInt(String.valueOf(move.charAt(4)));

        if(checkIfThereIsPawn(coordinates.get(newY).get(newX))) {
            return false;
        }

        if(moveLength(startX, startY, newX, newY) == 2) { // when the pawn is moving 2 squares it must capture opponents pawn
            int middleX = (newX + startX) / 2;            // xPosition which moving pawn is jumping over
            int middleY = (newY + startY) / 2;            // yPosition which moving pawn is jumping over
            if(!checkIfThereIsPawn(coordinates.get(middleY).get(middleX))) {  // check if there isn't any pawn
                return false; // immediately return false
            } else if(coordinates.get(middleY).get(middleX).getColour()==coordinates.get(startY).get(startX).getColour()) {// check if pawns are the same color
                    return false; // if there is pawn, but it's the same color, also return false
            }
        }

        return true;
    }

    private boolean checkIfThereIsPawn(Piece piece) {
        if (piece==null) { // if there is no piece
            return false;
        } else {           // if there is piece of any color
            return true;
        }
    }

    private int moveLength(int startX, int startY, int endX, int endY) {
        int deltaX = Math.abs(startX - endX);
        int deltaY = Math.abs(startY - endY);
        double distance = Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY, 2));
        if (distance < 2) {
            return 1;
        }
        if (distance > 2) {
            return 2;
        }
        return 0;
    }

    private void movePawns(String move) {
        // read all the values from string
        int startX = Integer.parseInt(String.valueOf(move.charAt(0)));
        int startY = Integer.parseInt(String.valueOf(move.charAt(1)));
        int newX = Integer.parseInt(String.valueOf(move.charAt(3)));
        int newY = Integer.parseInt(String.valueOf(move.charAt(4)));

        // convert int to squares
        Square start = new Square (startX, startY);
        Square newSquare = new Square (newX, newY);

        // move piece from one square to another
        board.movePiece(start, newSquare);
    }

    // setup functions are setting up input and outputs from users, they also send info to client which color they play
    private void setupWhite() throws IOException {
        inputW = new Scanner(whitePlayer.getInputStream());
        outputW = new PrintWriter(whitePlayer.getOutputStream(), true);
        outputW.println("White");
    }
    private void setupBlack() throws IOException {
        inputB = new Scanner(blackPlayer.getInputStream());
        outputB = new PrintWriter(blackPlayer.getOutputStream(), true);
        outputB.println("Black");
    }
}
