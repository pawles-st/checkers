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
    Player whitePlayer;
    Player blackPlayer;
    Scanner scanner;
    PrintWriter writer;
    PrintWriter writerOpponent;
    Board board;
    ClientView cView;
    Boolean whiteTurn = true;
    Game(Socket firstPlayer, Socket secondPlayer) {
        this.whitePlayer = new Player(firstPlayer, secondPlayer, Colour.WHITE, Colour.BLACK);
        this.blackPlayer = new Player(secondPlayer, firstPlayer, Colour.BLACK, Colour.WHITE);

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
                    Turn(whitePlayer); // it's whitePlayer turn, and blackPlayer is opponent
                } else {
                    Turn(blackPlayer); // it's blackPlayer turn, and whitePlayer is opponent
                }
                cView.drawBoard(board); // after every move draw current board status on server terminal
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private void Turn(Player player) throws IOException {
        scanner = new Scanner(player.getSocket().getInputStream());
        writer = new PrintWriter(player.getSocket().getOutputStream(), true);
        writerOpponent = new PrintWriter(player.getOpponent().getOutputStream(), true);

        String line;
        Boolean start = whiteTurn;
        Boolean killIsPossible = KillPossible(player);

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

            if(killIsPossible && result.getType()!=MoveType.KILL) {
                System.out.println("Move type should be KILL");
                writer.println("incorrect"); // send info to client, that his move cannot be done
                continue;
            }

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

    private Boolean KillPossible(Player player) {
        Colour playerColor = player.getColor();
        List<List<AbstractPiece>> coordinates = board.getCoordinates();

        for(int y=0; y<8; y++) {
            for(int x=0; x<8; x++) {
                if(coordinates.get(y).get(x) != null) {
                    if(coordinates.get(y).get(x).getColour() == playerColor) {
                        if(CheckKill(x, y, player)) {
                            System.out.println("Kill is possible");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Boolean CheckKill(int x, int y, Player player) {
        List<List<AbstractPiece>> coordinates = board.getCoordinates();
        Boolean northPossible = false;
        Boolean southPossible = false;
        Boolean eastPossible = false;
        Boolean westPossible = false;
        if(y<=5) {northPossible = true;}
        if(y>=2) {southPossible = true;}
        if(x<=5) {eastPossible = true;}
        if(x>=2) {westPossible = true;}
        if(northPossible && eastPossible) {
            if (coordinates.get(y+1).get(x+1) != null && coordinates.get(y+2).get(x+2) == null) {
                if (coordinates.get(y+1).get(x+1).getColour() == player.getOpponentColor()) {
                    return true;
                }
            }
        }
        if(northPossible && westPossible) {
            if (coordinates.get(y+1).get(x-1) != null && coordinates.get(y+2).get(x-2) == null) {
                if (coordinates.get(y+1).get(x-1).getColour() == player.getOpponentColor()) {
                    return true;
                }
            }
        }
        if(southPossible && eastPossible) {
            if (coordinates.get(y-1).get(x+1) != null && coordinates.get(y-2).get(x+2) == null) {
                if (coordinates.get(y-1).get(x+1).getColour() == player.getOpponentColor()) {
                    return true;
                }
            }
        }
        if(southPossible && westPossible) {
            if (coordinates.get(y-1).get(x-1) != null && coordinates.get(y-2).get(x-2) == null) {
                if (coordinates.get(y-1).get(x-1).getColour() == player.getOpponentColor()) {
                    return true;
                }
            }
        }
        return false;
    }

    private MoveResult tryMove(MoveData data) {
        List<List<AbstractPiece>> coordinates = board.getCoordinates();

        if(coordinates.get(data.getStartY()).get(data.getStartX()) == null) { // if there isn't any pawn at start position
            return new MoveResult(MoveType.NONE); // move cannot be done
        }

        if(whiteTurn && coordinates.get(data.getStartY()).get(data.getStartX()).getColour() == Colour.BLACK) { // if it's whitePlayer's turn and pawn at starting position is black
            return new MoveResult(MoveType.NONE); // move cannot be done
        }

        if(!whiteTurn && coordinates.get(data.getStartY()).get(data.getStartX()).getColour() == Colour.WHITE) { // if it isn't whitePlayer's turn and pawn at starting position is white
            return new MoveResult(MoveType.NONE); // move cannot be done
        }

        if(checkIfThereIsPawn(coordinates.get(data.getNewY()).get(data.getNewX()))) { // if there is pawn at new coordinates
            return new MoveResult(MoveType.NONE); // move cannot be done
        }

        if(Math.abs(data.getNewX() - data.getStartX()) == 1 && Math.abs(data.getNewY() - data.getStartY()) == 1) { // if pawn is moving 1 square
            return new MoveResult(MoveType.NORMAL); // move will be normal
        }

        if(Math.abs(data.getNewX() - data.getStartX()) == 2 && Math.abs(data.getNewY() - data.getStartY()) == 2) { // if pawn is moving 2 squares
            int middleX = (data.getNewX() + data.getStartX()) / 2;            // xPosition which moving pawn is jumping over
            int middleY = (data.getNewY() + data.getStartY()) / 2;            // yPosition which moving pawn is jumping over
            if(coordinates.get(middleY).get(middleX) != null) {               // check if pawn will be jumping over other pawn
                if (coordinates.get(middleY).get(middleX).getColour() != coordinates.get(data.getStartY()).get(data.getStartX()).getColour()) { // if pawn in between is different color return kill move
                    return new MoveResult(MoveType.KILL);
                }
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
        firstOuput = new PrintWriter(whitePlayer.getSocket().getOutputStream(), true);
        firstOuput.println("White");
        System.out.println("First Player received white color");

        PrintWriter secondOutput;
        secondOutput = new PrintWriter(blackPlayer.getSocket().getOutputStream(), true);
        secondOutput.println("Black");
        System.out.println("Second Player received black color");
    }
}
