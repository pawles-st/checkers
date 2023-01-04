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
    Socket blackPlayer;
    Scanner scanner;
    PrintWriter writer;
    PrintWriter writerOpponent;
    Board board;
    ClientView cView;

    Boolean whiteTurn = true;
    Game(Socket firstPlayer, Socket secondPlayer) {
        this.whitePlayer = firstPlayer;
        this.blackPlayer = secondPlayer;

        BoardDirector director = new BoardDirector();
        director.setBoardBuilder(new BrazilianBoardBuilder());
        director.buildBoard();
        board = director.getBoard();

        cView = new ClientView();

    }

    @Override
    public void run() {
        try {
            setup();
            while(!gameLost()) {
                if(whiteTurn) {
                    Turn(whitePlayer, blackPlayer); // it's whitePlayer turn, and blackPlayer is opponent
                } else {
                    Turn(blackPlayer, whitePlayer); // it's blackPlayer turn, and whitePlayer is opponent
                }
                cView.drawBoard(board); // after every move draw current board status on server terminal
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private void Turn(Socket player, Socket opponent) throws IOException {
        scanner = new Scanner(player.getInputStream());
        writer = new PrintWriter(player.getOutputStream(), true);
        writerOpponent = new PrintWriter(opponent.getOutputStream(), true);

        String line;
        Boolean start = whiteTurn;

        writer.println("your turn");
        do {
            if(whiteTurn) {
                System.out.println("Waiting for whitePlayer input");
            }else{
                System.out.println("Waiting for blackPlayer input");
            }
            // reading what move to do from whitePlayer
            line = scanner.nextLine();
            // outputing it on server
            System.out.println("White input: "+line);

            MoveData data = new MoveData(line);
            MoveResult result = tryMove(data);

            switch (result.getType()) {
                case NONE:
                    System.out.println("Move type is NONE");
                    writer.println("incorrect"); // send info to client, that his move cannot be done
                    break;
                case NORMAL:
                    System.out.println("Move type is NORMAL");
                    movePawns(data);       // do it
                    whiteTurn = !whiteTurn;              // switch turn to the second player
                    writerOpponent.println(line); // send the move to the second player
                    break;
                case KILL:
                    System.out.println("Move type is KILL");
                    movePawns(data);       // do it
                    killPawn(data);
                    whiteTurn = !whiteTurn;              // switch turn to the second player
                    writerOpponent.println(line); // send the move to the second player
                    break;
            }
        } while (start == whiteTurn);
        writer.println("correct"); // send info to client, that the move is correct and will be done
    }

    private boolean gameLost() {
        //TODO something like whitePlayer.pawns==0 || blackPlayer.pawns==0

        return false;
    }

    private MoveResult tryMove(MoveData data) {
        List<List<AbstractPiece>> coordinates = board.getCoordinates();

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

    private boolean checkIfThereIsPawn(AbstractPiece piece) {
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
    //setup sends to client what colors they're playing
    private void setup() throws IOException {
        PrintWriter firstOuput;
        firstOuput = new PrintWriter(whitePlayer.getOutputStream(), true);
        firstOuput.println("White");

        PrintWriter secondOutput;
        secondOutput = new PrintWriter(blackPlayer.getOutputStream(), true);
        secondOutput.println("Black");
    }
}
