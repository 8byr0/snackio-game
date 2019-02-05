package fr.esigelec.snackio.ui;

import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.SoloGameEngine;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.networking.Position;
import fr.esigelec.snackio.ui.customButtons.AnimatedButton;
import fr.esigelec.snackio.ui.customToggles.CharacterPicker;
import fr.esigelec.snackio.ui.customToggles.MapPicker;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SoloConfigMenu implements Initializable {
   
    // FXML components
    @FXML
    private AnimatedButton openJoinServerMenuButton;
    @FXML
    private TextField playerName;
    @FXML
    private AnchorPane anchor, mainAnchorPane;
    @FXML
    private GridPane grid;

    // Custom toggles
    private CharacterPicker characterSelector = new CharacterPicker();
    private MapPicker mapSelector = new MapPicker();

    // UI animation
    private FadeTransition showAnchor;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MAIN_MENU);
        renderCharacterSelectorToggle();
        renderMapSelectorToggle();

        animation(1, 0);
        openJoinServerMenuButton.setOnAction(this::connection);

    }

    private void connection(ActionEvent actionEvent) {
        if (!playerName.getText().isEmpty() && mapSelector.getSelectedToggle() != null && characterSelector.getSelectedToggle() != null) {
            launchGame();
        }
    }

    private void renderCharacterSelectorToggle() {
        grid.add(characterSelector, 1, 1);
    }

    private void renderMapSelectorToggle() {
        grid.add(mapSelector, 1, 2);
    }

    private void launchGame() {
        try {
            SnackioGame game = SnackioGame.getInstance();

            ToggleButton rbCha = characterSelector.getSelectedToggle();
            ToggleButton rbMap = mapSelector.getSelectedToggle();


            Player myPlayer = new Player(playerName.getText(), CharacterFactory.CharacterType.valueOf(String.valueOf(rbCha.getId())));

            // TODO give a random position instead
            myPlayer.setPosition(new Position(100, 100));

            AbstractGameEngine engine = new SoloGameEngine(game, myPlayer, MapFactory.MapType.valueOf(String.valueOf(rbMap.getId())), 5);
            //MenuController.getStage().initModality(APPLICATION_MODAL);
            engine.startGame();
        } catch (GameCannotStartException | UnhandledCharacterTypeException | UnhandledControllerException | NoCharacterSetException e) {
            Log.error(e.getMessage(), e);
        }
    }

    public void animation(double startValue, double endValue) {
        anchor.setStyle("-fx-opacity:" + startValue);
        anchor.setDisable(true);
        showAnchor = new FadeTransition(Duration.millis(1000), anchor);
        showAnchor.setFromValue(startValue);

        showAnchor.setToValue(endValue);

        showAnchor.setAutoReverse(true);

        showAnchor.setCycleCount(500);

        showAnchor.setDuration(Duration.INDEFINITE);

        showAnchor.play();

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    showAnchor.stop();
                    mainAnchorPane.getChildren().remove(anchor);
                }));
        timeline.play();


    }
}
