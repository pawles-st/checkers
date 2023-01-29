package org.pawles.checkers.server;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.pawles.checkers.client.ClientView;
import org.pawles.checkers.objects.*;
import org.pawles.checkers.utils.BoardDirector;
import org.pawles.checkers.utils.BrazilianBoardBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Game thread for checkers.
 *
 * @author Szymon
 * @version 1.0
 */
public class Game implements Runnable {
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Board board;
    private final ClientView cView;
    private Boolean whiteTurn = true;
    private final int boardSize;

    /**
     *
     * @param firstPlayer - first player connected to server, plays white color
     * @param secondPlayer - second player connected to server, plays black color
     * @param boardSize - board size on which players will play
     */
    Game(final Socket firstPlayer, final Socket secondPlayer, final int boardSize) {
        this.whitePlayer = new Player(firstPlayer, secondPlayer, Colour.WHITE, boardSize);
        this.blackPlayer = new Player(secondPlayer, firstPlayer, Colour.BLACK, boardSize);
        this.boardSize = boardSize;

        BoardDirector director = new BoardDirector();
        director.setBoardBuilder(new BrazilianBoardBuilder());
        director.buildBoard(boardSize);
        board = director.getBoard();

        cView = new ClientView();
    }

    @Override
    public void run() {
        try {
            setup();
            while (!gameLost()) {
                if (whiteTurn) {
                    Turn(whitePlayer); // it's whitePlayer turn, and blackPlayer is opponent
                } else {
                    Turn(blackPlayer); // it's blackPlayer turn, and whitePlayer is opponent
                }
                cView.drawBoard(board); // after every move draw current board status on server terminal
            }
            System.out.println("Match ended");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    /**
     *
     * @param player - player whose turn is right now
     * @throws IOException - thrown when function cannot get player's scanner, printwriter and his opponents printwriter
     */
    private void Turn(final Player player) throws IOException {
        Scanner scanner = new Scanner(player.getSocket().getInputStream());
        PrintWriter writer = new PrintWriter(player.getSocket().getOutputStream(), true);
        PrintWriter writerOpponent = new PrintWriter(player.getOpponent().getOutputStream(), true);
        boolean retryMove = false;

        String line;
        boolean start = whiteTurn;
        boolean killIsPossible = KillPossible(player);

        do {
            List<List<AbstractPiece>> coordinates = board.getCoordinates();
            if (!retryMove) {
                writer.println("your turn");
            }
            if (whiteTurn) {
                System.out.println("Waiting for whitePlayer input");
            } else {
                System.out.println("Waiting for blackPlayer input");
            }
            // reading what move to do from whitePlayer
            line = scanner.nextLine();
            // outputing it on server
            System.out.println("Player input: " + line);

            MoveData data = new MoveData(line);
            MoveType result = tryMove(data);

            if (killIsPossible && result != MoveType.KILL) {
                System.out.println("Move type should be KILL");
                writer.println("incorrect"); // send info to client, that his move cannot be done
                retryMove = true;
                continue;
            }

            switch (result) {
                case NONE -> {
                    System.out.println("Move type is NONE");
                    writer.println("incorrect"); // send info to client, that his move cannot be done
                }
                case NORMAL -> {
                    System.out.println("Move type is NORMAL");
                    movePawns(data);       // do it
                    whiteTurn = !whiteTurn;              // switch turn to the second player
                    writerOpponent.println(line); // send the move to the second player
                }
                case KILL -> {
                    System.out.println("Move type is KILL");
                    movePawns(data);       // do it
                    killPawn(data);
                    if (whiteTurn) {
                        blackPlayer.removePieceFromBoard();
                        System.out.println("Black has now: " + blackPlayer.getPieces() + " pieces");
                    } else {
                        whitePlayer.removePieceFromBoard();
                        System.out.println("White has now: " + whitePlayer.getPieces() + " pieces");
                    }
                    if (MoveSimulator.tryToKill(coordinates, data.getNewX(), data.getNewY(), boardSize)) {
                        System.out.println("Player can do another kill");
                        writerOpponent.println(line); // send the move to the second player
                    } else {
                        System.out.println("Player cannot do any more kills");
                        whiteTurn = !whiteTurn;       // switch turn to the second player
                        writerOpponent.println(line); // send the move to the second player
                    }
                    retryMove = false;
                }
            }
            if (!retryMove) {
                writer.println("correct"); // send info to client, that the move is correct and will be done
            }
        } while (start == whiteTurn);
    }

    /**
     * @return - true when one of players doesn't have any pieces left
     */
    private boolean gameLost() {
        if (whitePlayer.getPieces() == 0) {
            System.out.println("Black player won!");
            return true;
        } else if (blackPlayer.getPieces() == 0) {
            System.out.println("White player won!");
            return true;
        }
        return false;
    }

    /**
     * @param player - player for which function will look for kill
     * @return - true when given player can kill opponents piece
     */
    private Boolean KillPossible(final Player player) {
        Colour playerColor = player.getColor();
        List<List<AbstractPiece>> coordinates = board.getCoordinates();

        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                if (coordinates.get(y).get(x) != null) {
                    if (coordinates.get(y).get(x).getColour() == playerColor) {
                        if (MoveSimulator.tryToKill(coordinates, x, y, boardSize)) {
                            System.out.println("Kill is possible");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param data - from where and where to move
     * @return - check's what move is player trying to do : NONE, NORMAL or KILL
     */
    private MoveType tryMove(final MoveData data) {
        List<List<AbstractPiece>> coordinates = board.getCoordinates();

        AbstractPiece playersPiece = coordinates.get(data.getStartY()).get(data.getStartX());
        boolean isKing = playersPiece.isKing(playersPiece);
        boolean goingUp = data.getNewY() > data.getStartY();
        boolean goingRight = data.getNewX() > data.getStartX();
        int moveLength = Math.abs(data.getNewX() - data.getStartX());

        System.out.println("UP: " + goingUp + " Right: " + goingRight + " King: " + isKing + " MoveLength: " + moveLength);

        if (whiteTurn && playersPiece.getColour() == Colour.BLACK) { // if it's whitePlayer's turn and pawn at starting position is black
            System.out.println("White player is trying to move black piece");
            return MoveType.NONE; // move cannot be done
        }

        if (!whiteTurn && playersPiece.getColour() == Colour.WHITE) { // if it isn't whitePlayer's turn and pawn at starting position is white
            System.out.println("Black player is trying to move white piece");
            return MoveType.NONE; // move cannot be done
        }

        if (playersPiece.getColour() == Colour.WHITE && data.getNewY() == data.getStartY()-1) {
            System.out.println("White player is trying to move backwards");
            return MoveType.NONE; // move cannot be done
        }

        if (playersPiece.getColour() == Colour.BLACK && data.getNewY() == data.getStartY()+1) {
            System.out.println("Black player is trying to move backwards");
            return MoveType.NONE; // move cannot be done
        }

        return MoveSimulator.simulate(goingUp, goingRight, data.getStartX(), data.getStartY(), moveLength, coordinates);
    }

    /**
     * @param data - data about the move (which piece and where to move)
     */
    private void movePawns(final MoveData data) {
        // convert int to squares
        Square start = SquareInstancer.getInstance(data.getStartX(), data.getStartY());
        //System.out.println("Starting square: "+data.getStartX()+""+data.getStartY());
        Square newSquare = SquareInstancer.getInstance(data.getNewX(), data.getNewY());
        //System.out.println("Ending square: "+data.getNewX()+""+data.getNewY());

        // move piece from one square to another
        board.movePiece(start, newSquare);
    }

    private void killPawn(final MoveData data) {
        int middleX = (data.getNewX() + data.getStartX()) / 2;
        int middleY = (data.getNewY() + data.getStartY()) / 2;
        System.out.println(middleX + "" + middleY);
        Square middleSquare = SquareInstancer.getInstance(middleX, middleY);
        board.deletePiece(middleSquare);
    }
    //setup sends to client what colors they're playing

    /**
     * Send info about colors to players.
     * @throws IOException when player print writer can not be recognized
     */
    private void setup() throws IOException {
        PrintWriter firstOutput;
        firstOutput = new PrintWriter(whitePlayer.getSocket().getOutputStream(), true);
        firstOutput.println(boardSize);
        firstOutput.println("White");
        System.out.println("First Player received white color");

        PrintWriter secondOutput;
        secondOutput = new PrintWriter(blackPlayer.getSocket().getOutputStream(), true);
        secondOutput.println(boardSize);
        secondOutput.println("Black");
        System.out.println("Second Player received black color");

        // setup db connection

        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder registry = new StandardServiceRegistryBuilder();
        registry.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = registry.build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();

    }
}
