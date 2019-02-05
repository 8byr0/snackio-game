package fr.esigelec.snackio.ui.customToggles;

import fr.esigelec.snackio.game.character.CharacterFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CharacterPicker extends HBox {
    private static final int SQUARE_SIDE = 67;
    private ToggleGroup characterGroup = new ToggleGroup();
    private ToggleButton chosenCharacter;

    public CharacterPicker() {
        List<Enum> somethingList = Arrays.asList(CharacterFactory.CharacterType.values());
//        HBox characterBox = new HBox();
        this.setSpacing(5);
        for (Enum character : somethingList) {
            ToggleButton rbCha = new ToggleButton();
            ImageView imgCha = new ImageView("sprites/menu_" + character.toString() + ".png");
            imgCha.setFitHeight(SQUARE_SIDE);
            imgCha.setFitWidth(SQUARE_SIDE);
            rbCha.setGraphic(imgCha);
            rbCha.setId(character.toString());
            rbCha.setToggleGroup(characterGroup);
            rbCha.setStyle("-fx-background-color: white");
            rbCha.setStyle("-fx-arc-width: 0");
            rbCha.setStyle("-fx-arc-height: 0");
            this.getChildren().add(rbCha);
        }

        characterGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (characterGroup.getSelectedToggle() != null) {
                    if (chosenCharacter != null) {
                        chosenCharacter.setStyle("-fx-border-color: white");
                    }
                    chosenCharacter = (ToggleButton) characterGroup.getSelectedToggle();
                    ((ToggleButton) characterGroup.getSelectedToggle()).setStyle("-fx-background-color: turquoise");
                }
            }
        });
    }

    public ToggleButton getSelectedToggle() {
        return this.chosenCharacter;
    }
}
