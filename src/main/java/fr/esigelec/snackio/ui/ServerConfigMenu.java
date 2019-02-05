package fr.esigelec.snackio.ui;

import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import fr.esigelec.snackio.networking.server.SnackioNetServer;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;
import java.util.List;

public class ServerConfigMenu implements Initializable {

    private static final int SQUARE_SIDE = 67;
    @FXML
    private TextField playerName;

    @FXML
    private TextField serverName;

    @FXML
    private ColumnConstraints mapsFitLimit, characterFitLimit;

    @FXML
    private Button submit;

    private CharacterPicker characterSelector = new CharacterPicker();
    private MapPicker mapSelector = new MapPicker();

    private FadeTransition hideAnchor;
    @FXML
    private AnchorPane anchor, mainAnchorPane;
    @FXML
    private GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        showImageMap();
        showImageCharacter();
        animation(1, 0);
        submit.setOnMouseEntered(event -> {
            if(mapGroup.getSelectedToggle() != null && characterGroup.getSelectedToggle() != null && !playerName.getText().isEmpty() && !serverName.getText().isEmpty()) {
                submit.setOnAction(this::submitServer);
                submit.setTranslateX(1);
                submit.setStyle("-fx-opacity: 1");
            }
        });
        submit.setOnMouseExited(event -> {
            submit.setStyle("-fx-opacity: 0.6");
            submit.setTranslateX(0);
        });
// Introduction

    }

    private void showImageCharacter() {
        grid.add(characterSelector, 1, 3);
    }

    private void showImageMap() {

        grid.add(mapSelector, 1, 1);
//        grid.add(mapNameBox, 1, 2);

    }

    public void submitServer(ActionEvent actionEvent) {
        if (mapSelector.getSelectedToggle() != null && characterSelector.getSelectedToggle() != null
                && !playerName.getText().isEmpty() && !serverName.getText().isEmpty()) {
            try {
                SnackioGame game = SnackioGame.getInstance();
                // Create the local player
                ToggleButton rbCha = (ToggleButton) characterSelector.getSelectedToggle();
                Player myPlayer = new Player(playerName.getText(),
                        CharacterFactory.CharacterType.valueOf(String.valueOf(rbCha.getId())));

                // Instantiate Network game engine to control gameplay
                ToggleButton rbMap = (ToggleButton) mapSelector.getSelectedToggle();
                AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer,
                        MapFactory.MapType.valueOf(String.valueOf(rbMap.getId())));
                // Instantiate a NetClient to exchange with client
                SnackioNetClient cli = new SnackioNetClient(engine);
                NetworkGameEngine nEngine = new NetworkGameEngine(game, myPlayer,
                        MapFactory.MapType.valueOf(String.valueOf(rbMap.getId())));
                List<InetAddress> servers = cli.getAvailableServers();
                String[] difficultWords = new String[10];
                difficultWords[0] = "you";

                SnackioNetServer.main(difficultWords);

                engine.startGame();

            } catch (Exception e) {
                Log.error(e.getMessage(), e);

            }
        } else {
            Label notCompleted = new Label("Not completed!!");
            notCompleted.setTextFill(Color.web("#0076a3"));
            grid.add(notCompleted, 0, 4);
        }
    }

    public void animation(double startValue, double endValue) {
        anchor.setStyle("-fx-opacity:" + startValue);
        anchor.setDisable(true);
        hideAnchor = new FadeTransition(Duration.millis(1000), anchor);
        hideAnchor.setFromValue(startValue);

        hideAnchor.setToValue(endValue);

        hideAnchor.setAutoReverse(true);

        hideAnchor.setCycleCount(500);

        hideAnchor.setDuration(Duration.INDEFINITE);

        hideAnchor.play();

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    hideAnchor.stop();
                    mainAnchorPane.getChildren().remove(anchor);
                }));
        timeline.play();


    }
}

