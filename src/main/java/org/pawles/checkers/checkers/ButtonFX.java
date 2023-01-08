package org.pawles.checkers.checkers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ButtonFX extends Button {

    public ButtonFX() {
        super("Register move");
        this.setMinSize(800, 100);
        this.relocate(0, 800);
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println("Button has been pressed");
            }
        });
    }
}
