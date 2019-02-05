package fr.esigelec.snackio.ui.customButtons;

import javafx.scene.control.Button;

public class AnimatedButton extends Button {
    public AnimatedButton() {

        this.setOnMouseEntered(event -> {
            this.setTranslateX(1);
            this.setStyle("-fx-opacity: 1");
        });
        this.setOnMouseExited(event -> {
            this.setStyle("-fx-opacity: 0.6");
            this.setTranslateX(0);
        });
    }
}
