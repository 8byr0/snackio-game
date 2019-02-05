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
import fr.esigelec.snackio.ui.customToggles.CharacterPicker;
import fr.esigelec.snackio.ui.customToggles.MapPicker;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SoloConfigMenu implements Initializable {
    private static final int SQUARE_SIDE = 70;
    @FXML
    private Button join;

    private CharacterPicker characterSelector = new CharacterPicker();
    private MapPicker mapSelector = new MapPicker();

    private Player myPlayer;
    private Timeline timeline;

    @FXML
    private AnchorPane anchor, mainAnchorPane;

    private ToggleButton perChoice, mapChoice;

    private FadeTransition showAnchor;

    @FXML
    private GridPane grid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MAIN_MENU);
        showImageCharacter();
        showImageMap();
        animation(1, 0);
        join.setOnMouseEntered(event -> {
            if(mapGroup.getSelectedToggle() !=null && characterGroup.getSelectedToggle() != null) {
                join.setTranslateX(1);
                join.setStyle("-fx-opacity: 1");
                join.setOnAction(this::connection);
            }
        });
        join.setOnMouseExited(event -> {
            join.setStyle("-fx-opacity: 0.6");
            join.setTranslateX(0);
        });

    }

    public void connection(ActionEvent actionEvent) {
        if (!playerName.getText().isEmpty() && mapSelector.getSelectedToggle() != null && characterSelector.getSelectedToggle() != null) {
            startGameSolo();
        }
    }

    public void showImageCharacter() {
        //        //show all the choices of the characters

        grid.add(characterSelector, 1, 1);

    }

    private void showImageMap() {
        grid.add(mapSelector, 1, 2);
    }

    private void startGameSolo() {
        try {
            SnackioGame game = SnackioGame.getInstance();

            ToggleButton rbCha = characterSelector.getSelectedToggle();
            ToggleButton rbMap = mapSelector.getSelectedToggle();


            myPlayer = new Player(playerName.getText(), CharacterFactory.CharacterType.valueOf(String.valueOf(rbCha.getId())));

            myPlayer.getCharacter().setPosition(100, 900);

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

        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    showAnchor.stop();
                    mainAnchorPane.getChildren().remove(anchor);
                }));
        timeline.play();


    }
}
