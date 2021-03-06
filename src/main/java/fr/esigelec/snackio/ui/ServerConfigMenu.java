package fr.esigelec.snackio.ui;

import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import fr.esigelec.snackio.networking.server.SnackioNetServer;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;

public class ServerConfigMenu implements Initializable {

    private static final int SQUARE_SIDE = 67;
    @FXML
    private TextField playerNameField;

    @FXML
    private TextField serverName;

    @FXML
    private ColumnConstraints mapsFitLimit, characterFitLimit;

    @FXML
    private AnimatedButton createServerButton;

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
        renderMapSelector();
        renderCharacterSelector();
        animation(1, 0);

        // Introduction
        createServerButton.setOnAction(this::createServer);
    }

    private void renderCharacterSelector() {
        grid.add(characterSelector, 1, 3);
    }

    private void renderMapSelector() {
        grid.add(mapSelector, 1, 1);
//        grid.add(mapNameBox, 1, 2);
    }

    private void createServer(ActionEvent actionEvent) {
        if (mapSelector.getSelectedToggle() != null && characterSelector.getSelectedToggle() != null
                && !playerNameField.getText().isEmpty() && !serverName.getText().isEmpty()) {
            try {
                Player thePlayer = new Player(playerNameField.getText(), characterSelector.getSelectedCharacter());


                // Instantiate Network game engine to control gameplay
                AbstractGameEngine engine = new NetworkGameEngine(SnackioGame.getInstance(), thePlayer);

                // Instantiate a NetClient to exchange with client
                SnackioNetClient cli = new SnackioNetClient();

                cli.setOnConnectionSuccessfull((gameState) -> {
                    try {
                        engine.setGameState(gameState);
                        engine.startGame();
                    } catch (GameCannotStartException e) {
                        e.printStackTrace();
                    }
                });

                Thread serverThread = new Thread(()->{
                    try {
                        Log.set(Log.LEVEL_DEBUG);
                        SnackioNetServer srv = new SnackioNetServer(mapSelector.getSelectedMap(), serverName.getText());
                        srv.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                serverThread.start();
                cli.connectServer((InetAddress.getByName("127.0.0.1")), engine);


//                engine.startGame();

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

