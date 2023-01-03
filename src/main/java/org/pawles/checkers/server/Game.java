package org.pawles.checkers.server;

import org.pawles.checkers.client.ClientView;
import org.pawles.checkers.objects.*;
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
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

                        MoveData data = new MoveData(line);
                        MoveResult result = tryMove(data);

                        switch (result.getType()) {
                            case NONE:
                                System.out.println("Move type is NONE");
                                outputW.println("incorrect"); // send info to client, that his move cannot be done
                                break;
                            case NORMAL:
                                System.out.println("Move type is NORMAL");
                                movePawns(data);       // do it
                                turn = 2;              // switch turn to the second player
                                outputB.println(line); // send the move to the second player
                                break;
                            case KILL:
                                System.out.println("Move type is KILL");
                                movePawns(data);       // do it
                                killPawn(data);
                                turn = 2;              // switch turn to the second player
                                outputB.println(line); // send the move to the second player
                                break;
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

                        MoveData data = new MoveData(line);
                        MoveResult result = tryMove(data);

                        switch (result.getType()) {
                            case NONE:
                                System.out.println("Move type is NONE");
                                outputB.println("incorrect"); // send info to client, that his move cannot be done
                                break;
                            case NORMAL:
                                System.out.println("Move type is NORMAL");
                                movePawns(data);       // do it
                                turn = 1;              // switch turn to the second player
                                outputW.println(line); // send the move to the second player
                                break;
                            case KILL:
                                System.out.println("Move type is KILL");
                                movePawns(data);       // do it
                                killPawn(data);
                                turn = 1;              // switch turn to the second player
                                outputW.println(line); // send the move to the second player
                                break;
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

    private MoveResult tryMove(MoveData data) {
        List<List<Piece>> coordinates = board.getCoordinates();

        if(checkIfThereIsPawn(coordinates.get(data.getNewY()).get(data.getNewX()))) { // if there is pawn at new coordinates
            return new MoveResult(MoveType.NONE); // move cannot be done
        }

        if(Math.abs(data.getNewX() - data.getStartX()) == 1 && Math.abs(data.getNewY() - data.getStartY()) == 1) { // if pawn is moving 1 square
            return new MoveResult(MoveType.NORMAL); // move will be normal
        }

        if(Math.abs(data.getNewX() - data.getStartX()) == 2 && Math.abs(data.getNewY() - data.getStartY()) == 2) { // if pawn is moving 2 squares
            int middleX = (data.getNewX() + data.getStartX()) / 2;            // xPosition which moving pawn is jumping over
            int middleY = (data.getNewY() + data.getStartY()) / 2;            // yPosition which moving pawn is jumping over
            if(coordinates.get(middleY).get(middleX).getColour() != coordinates.get(data.getStartY()).get(data.getStartX()).getColour()) { // if pawn in between is different color return kill move
                return  new MoveResult(MoveType.KILL);
            }
        }

        return new MoveResult(MoveType.NONE); // if none requirements were met, return none
    }

    private boolean checkIfThereIsPawn(Piece piece) {
        if (piece==null) { // if there is no piece
            return false;
        } else {           // if there is piece of any color
            return true;
        }
    }

    private void movePawns(MoveData data) {
        // convert int to squares
        Square start = new Square (data.getStartX(), data.getStartY());
        Square newSquare = new Square (data.getNewX(), data.getNewY());

        // move piece from one square to another
        board.movePiece(start, newSquare);
    }

    private void killPawn(MoveData data) {
        int middleX = (data.getNewX() + data.getStartX()) / 2;
        int middleY = (data.getNewY() + data.getStartY()) / 2;
        Square middleSquare = new Square(middleX, middleY);
        board.deletePiece(middleSquare);
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
