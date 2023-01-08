package org.pawles.checkers.checkers;

import javafx.scene.control.Button;
import org.pawles.checkers.client.GameCommunicator;

public class ButtonFX extends Button {

    public ButtonFX(GameCommunicator gameCommunicator) {
        super("Register move");
        this.setMinSize(800, 100);
        this.relocate(0, 800);
        this.setOnAction(e -> {
            System.out.println("Button has been pressed");
            gameCommunicator.waitForMove();
        });
    }
}
