package org.pawles.checkers.checkers;

import javafx.scene.control.Button;
import org.pawles.checkers.client.GameCommunicator;
import static org.pawles.checkers.checkers.CheckersApp.TILE_SIZE;

/**
 * Button class for registering a move
 * @author Szymon
 * @author pawles
 * @version 1.0
 */
public class ButtonFX extends Button {

    /**
     * constructs the "register move" button
     * @param gameCommunicator server communicator object
     */
    public ButtonFX(final GameCommunicator gameCommunicator, final int boardSize, boolean online) {
        super("Register move");
        this.setMinSize(boardSize*TILE_SIZE, TILE_SIZE);
        this.relocate(0, boardSize*TILE_SIZE);
        if (online) {
            this.setOnAction(e -> gameCommunicator.waitForMove());
        } else {
            this.setOnAction(e -> gameCommunicator.replayMove());
        }
    }
}
