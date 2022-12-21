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
                    System.out.println("Waiting for whitePlayer input");
                    outputW.println("your turn");
                    // reading what move to do from whitePlayer
                    line = inputW.nextLine();
                    System.out.println("White input: "+line);

                    if(MoveIsCorrect(line)) {  // check if the move can be done
                        movePawns(line);       // do it
                        turn = 2;              // switch turn to the second player
                    } else {
                        outputW.println("incorrect");
                        continue;
                    }
                    outputW.println("correct");
                } else if (turn==2) {
                    System.out.println("Waiting for blackPlayer input");
                    outputB.println("your turn");
                    // reading what move to do from blackPlayer
                    line = inputB.nextLine();
                    System.out.println("Black input: "+line);

                    if(MoveIsCorrect(line)) {  // check if the move can be done
                        movePawns(line);       // do it
                        turn = 1;              // switch turn to the first player
                    } else {
                        outputB.println("incorrect");
                        continue;
                    }
                    outputB.println("correct");
                }
                cView.drawBoard(board);
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

        //int startX = Integer.parseInt(String.valueOf(move.charAt(0)));
        //int startY = Integer.parseInt(String.valueOf(move.charAt(1)));

        int newX = Integer.parseInt(String.valueOf(move.charAt(3)));
        int newY = Integer.parseInt(String.valueOf(move.charAt(4)));

        if(checkIfThereIsPawn(coordinates.get(newY).get(newX))) {
            return false;
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

    private void movePawns(String move) {
        // ArrayList<ArrayList<Piece>> coordinates = board.getCoordinates();   // get current board status
        // assuming that move will look like 02:13 (which piece:where to move)
        // read all data about user move
        int startX = Integer.parseInt(String.valueOf(move.charAt(0)));
        int startY = Integer.parseInt(String.valueOf(move.charAt(1)));
        int newX = Integer.parseInt(String.valueOf(move.charAt(3)));
        int newY = Integer.parseInt(String.valueOf(move.charAt(4)));

        Square start = new Square (startX, startY);
        Square newSquare = new Square (newX, newY);

        //coordinates.get(startY).get(startX).move(start, newSquare);
        board.movePiece(start, newSquare);

    }

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
