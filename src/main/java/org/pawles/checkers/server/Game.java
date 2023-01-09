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

        AbstractPiece playersPiece = coordinates.get(data.getStartY()).get(data.getStartX());
        boolean isKing = playersPiece.isKing(playersPiece);
        boolean goingUp = data.getNewY() > data.getStartY();
        boolean goingRight = data.getNewX() > data.getStartX();
        int moveLength = Math.abs(data.getNewX() - data.getStartX());

        System.out.println("UP: "+goingUp+" Right: "+goingRight+" King: "+isKing+" MoveLength: "+moveLength);

        if(playersPiece == null) { // if there isn't any pawn at start position
            return new MoveResult(MoveType.NONE); // move cannot be done
        }

        if(whiteTurn && playersPiece.getColour() == Colour.BLACK) { // if it's whitePlayer's turn and pawn at starting position is black
            return new MoveResult(MoveType.NONE); // move cannot be done
        }

        if(!whiteTurn && playersPiece.getColour() == Colour.WHITE) { // if it isn't whitePlayer's turn and pawn at starting position is white
            return new MoveResult(MoveType.NONE); // move cannot be done
        }

        if(checkIfThereIsPawn(coordinates.get(data.getNewY()).get(data.getNewX()))) { // if there is pawn at new coordinates
            return new MoveResult(MoveType.NONE); // move cannot be done
        }

        if(!isKing) {
            //if (Math.abs(data.getNewX() - data.getStartX()) == 1 && Math.abs(data.getNewY() - data.getStartY()) == 1) { // if pawn is moving 1 square
            //    return new MoveResult(MoveType.NORMAL); // move will be normal
            //}
            if (allBetweenSquaresAreEmpty(goingUp, goingRight, data, moveLength)) {
                return new MoveResult(MoveType.NORMAL);
            }

            if (moveLength == 2) { // if pawn is moving 2 squares
                //int middleX = (data.getNewX() + data.getStartX()) / 2;            // xPosition which moving pawn is jumping over
                //int middleY = (data.getNewY() + data.getStartY()) / 2;            // yPosition which moving pawn is jumping over
                //if (coordinates.get(middleY).get(middleX) != null) {              // check if pawn will be jumping over other pawn
                    //if (coordinates.get(middleY).get(middleX).getColour() != playersPiece.getColour()) { // if pawn in between is different color return kill move
                    //    return new MoveResult(MoveType.KILL);
                    //}
                if(pawnAtSecondLastSquareAndOppositeColor(goingUp, goingRight, data)) {
                    return new MoveResult(MoveType.KILL);
                }
            }
        } else {
            //@TODO WHEN PIECE IS KING
            if (allBetweenSquaresAreEmpty(goingUp, goingRight, data, moveLength)) {
                return new MoveResult(MoveType.NORMAL);
            }
        }



        /*if(!isKing) {
            if (Math.abs(data.getNewX() - data.getStartX()) == 1 && Math.abs(data.getNewY() - data.getStartY()) == 1) { // if pawn is moving 1 square
                return new MoveResult(MoveType.NORMAL); // move will be normal
            }

            if (Math.abs(data.getNewX() - data.getStartX()) == 2 && Math.abs(data.getNewY() - data.getStartY()) == 2) { // if pawn is moving 2 squares
                int middleX = (data.getNewX() + data.getStartX()) / 2;            // xPosition which moving pawn is jumping over
                int middleY = (data.getNewY() + data.getStartY()) / 2;            // yPosition which moving pawn is jumping over
                if (coordinates.get(middleY).get(middleX) != null) {               // check if pawn will be jumping over other pawn
                    if (coordinates.get(middleY).get(middleX).getColour() != playersPiece.getColour()) { // if pawn in between is different color return kill move
                        return new MoveResult(MoveType.KILL);
                    }
                }
            }
        } else {
            int moveLength = Math.abs(data.getNewX() - data.getStartX());
            boolean goingUp = data.getNewY() > data.getStartY(); // when new Y is higher, king is going upwards
            boolean goingRight = data.getNewX() > data.getStartX(); // when new X is higher, king is going right
            if(goingUp && goingRight) {
                for(int i=1; i<=moveLength; i++) { // check every single tile in between
                    if(checkIfThereIsPawn(coordinates.get(data.getStartY()+i).get(data.getStartX()+i))) { // if there is pawn in between
                        if(i+1 != moveLength) { // and king isn't ending his movement at next tile
                            return new MoveResult(MoveType.NONE); // this move isn't valid
                        } else { // but if the move is ending at the next tile
                            return new MoveResult(MoveType.KILL); // this move will be kill move
                        }
                    } else { // if there aren't any pawns in between
                        return new MoveResult(MoveType.NORMAL); // this move is valid, but won't kill anything
                    }
                }
            } else if (!goingUp && goingRight) {
                for(int i=1; i<=moveLength; i++) { // check every single tile in between
                    if(checkIfThereIsPawn(coordinates.get(data.getStartY()+i).get(data.getStartX()+i))) { // if there is pawn in between
                        if(i+1 != moveLength) { // and king isn't ending his movement at next tile
                            return new MoveResult(MoveType.NONE); // this move isn't valid
                        } else { // but if the move is ending at the next tile
                            return new MoveResult(MoveType.KILL); // this move will be kill move
                        }
                    } else { // if there aren't any pawns in between
                        return new MoveResult(MoveType.NORMAL); // this move is valid, but won't kill anything
                    }
                }
            } else if (goingUp && !goingRight) {
                for(int i=1; i<=moveLength; i++) { // check every single tile in between
                    if(checkIfThereIsPawn(coordinates.get(data.getStartY()+i).get(data.getStartX()+i))) { // if there is pawn in between
                        if(i+1 != moveLength) { // and king isn't ending his movement at next tile
                            return new MoveResult(MoveType.NONE); // this move isn't valid
                        } else { // but if the move is ending at the next tile
                            return new MoveResult(MoveType.KILL); // this move will be kill move
                        }
                    } else { // if there aren't any pawns in between
                        return new MoveResult(MoveType.NORMAL); // this move is valid, but won't kill anything
                    }
                }
            } else if (!goingUp && !goingRight) {
                for(int i=1; i<=moveLength; i++) { // check every single tile in between
                    if(checkIfThereIsPawn(coordinates.get(data.getStartY()+i).get(data.getStartX()+i))) { // if there is pawn in between
                        if(i+1 != moveLength) { // and king isn't ending his movement at next tile
                            return new MoveResult(MoveType.NONE); // this move isn't valid
                        } else { // but if the move is ending at the next tile
                            return new MoveResult(MoveType.KILL); // this move will be kill move
                        }
                    } else { // if there aren't any pawns in between
                        return new MoveResult(MoveType.NORMAL); // this move is valid, but won't kill anything
                    }
                }
            }
        }*/

        return new MoveResult(MoveType.NONE); // if none requirements were met, return none
    }

    private boolean checkIfThereIsPawn(AbstractPiece piece) {
        if (piece==null) { // if there is no piece
            return false;
        } else {           // if there is piece of any color
            return true;
        }
    }

    private boolean pawnAtSecondLastSquareAndOppositeColor(boolean goingUp, boolean goingRight, MoveData moveData) {
        List<List<AbstractPiece>> coordinates = board.getCoordinates();
        Colour colour = coordinates.get(moveData.getStartY()).get(moveData.getStartX()).getColour();

        if(goingUp && goingRight) {
            if(checkIfThereIsPawn(coordinates.get(moveData.getNewY()-1).get(moveData.getNewX()-1))) {
                return colour != coordinates.get(moveData.getNewY()-1).get(moveData.getNewX()-1).getColour();
            }
        } else if (goingUp && !goingRight) {
            if(checkIfThereIsPawn(coordinates.get(moveData.getNewY()-1).get(moveData.getNewX()+1))) {
                return colour != coordinates.get(moveData.getNewY()-1).get(moveData.getNewX()+1).getColour();
            }
        } else if (!goingUp && goingRight) {
            if(checkIfThereIsPawn(coordinates.get(moveData.getNewY()+1).get(moveData.getNewX()-1))) {
                return colour != coordinates.get(moveData.getNewY()+1).get(moveData.getNewX()-1).getColour();
            }
        } else if (!goingUp && !goingRight) {
            if(checkIfThereIsPawn(coordinates.get(moveData.getNewY()+1).get(moveData.getNewX()+1))) {
                return colour != coordinates.get(moveData.getNewY()+1).get(moveData.getNewX()+1).getColour();
            }
        }
        return false;
    }

    private boolean allBetweenSquaresAreEmpty(boolean goingUp, boolean goingRight, MoveData moveData, int moveLength) {
        List<List<AbstractPiece>> coordinates = board.getCoordinates();

        if(goingUp && goingRight) {
            for (int i=1; i<moveLength; i++) {
                if(coordinates.get(moveData.getStartY()+i).get(moveData.getStartX()+i) != null) {
                    return false;
                } else {
                    int x = moveData.getStartX()+i;
                    int y = moveData.getStartY()+i;
                    System.out.println("Square: "+x+""+y+" is empty");
                }
            }
        } else if (goingUp && !goingRight) {
            for (int i=1; i<moveLength; i++) {
                if(coordinates.get(moveData.getStartY()+i).get(moveData.getStartX()-i) != null) {
                    return false;
                } else {
                    int x = moveData.getStartX()-i;
                    int y = moveData.getStartY()+i;
                    System.out.println("Square: "+x+""+y+" is empty");
                }
            }
        } else if (!goingUp && goingRight) {
            for (int i=1; i<moveLength; i++) {
                if(coordinates.get(moveData.getStartY()-i).get(moveData.getStartX()+i) != null) {
                    return false;
                } else {
                    int x = moveData.getStartX()+i;
                    int y = moveData.getStartY()-i;
                    System.out.println("Square: "+x+""+y+" is empty");
                }
            }
        } else if (!goingUp && !goingRight) {
            for (int i=1; i<moveLength; i++) {
                if(coordinates.get(moveData.getStartY()-i).get(moveData.getStartX()-i) != null) {
                    return false;
                } else {
                    int x = moveData.getStartX()-i;
                    int y = moveData.getStartY()-i;
                    System.out.println("Square: "+x+""+y+" is empty");
                }
            }
        }
        return true;
    }

    private void movePawns(MoveData data) {
        // convert int to squares
        Square start = SquareInstancer.getInstance(data.getStartX(), data.getStartY());
        Square newSquare = SquareInstancer.getInstance(data.getNewX(), data.getNewY());

        // move piece from one square to another
        board.movePiece(start, newSquare);
    }

    private void killPawn(MoveData data) {
        int middleX = (data.getNewX() + data.getStartX()) / 2;
        int middleY = (data.getNewY() + data.getStartY()) / 2;
        Square middleSquare = SquareInstancer.getInstance(middleX, middleY);
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
