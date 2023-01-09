package org.pawles.checkers.checkers;

import javafx.scene.control.Button;
import org.pawles.checkers.client.GameCommunicator;

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
    public ButtonFX(final GameCommunicator gameCommunicator) {
        super("Register move");
        this.setMinSize(800, 100);
        this.relocate(0, 800);
        this.setOnAction(e -> gameCommunicator.waitForMove());
    }
}
