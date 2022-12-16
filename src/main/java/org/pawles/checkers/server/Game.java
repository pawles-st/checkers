package org.pawles.checkers.server;

public class Game {
    private int[][] board = new int[8][8];  // == 0 empty field, == 1 white pawn on it, == 2 black pawn on it

    Game() {                                // constructor creates default checkers board
        for(int i=0; i<3; i++) {            // for rows from 1 to 3
            for(int j=0; j<4; j++) {        // up to 4 pawns per row
                if(i%2==0) {                // if row == 1 or 3
                    board[i][j*2] = 2;      // fill fields A, C, E, G with black pawns
                } else {                    // if row ==2
                    board[i][(j*2)+1] = 2;  // fill fields B, D, G, H with black pawns
                }
            }
        }

        for(int i=7; i>4; i--) {            // for rows from 8 to 6
            for(int j=0; j<4; j++) {        // up to 4 pawns per row
                if(i%2==0) {                // if row == 7
                    board[i][j*2] = 1;      // fill fields A, C, E, G with black pawns
                } else {                    // if row == 8 or 6
                    board[i][(j*2)+1] = 1;  // fill fields B, D, G, H with black pawns
                }
            }
        }
    }

    void returnBoard() {
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                System.out.print(board[i][j]+" ");
            }
            System.out.print("\n");
        }
    }
}
