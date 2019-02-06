package fr.esigelec.snackio.ui;

import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.state.MultiplayerGameState;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import fr.esigelec.snackio.ui.customButtons.AnimatedButton;
import fr.esigelec.snackio.ui.customToggles.CharacterPicker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class JoinRoomMenu implements Initializable {
    @FXML
    public Label refreshServerInfo;
    @FXML
    private SplitPane alfa;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private AnimatedButton joinServerButton;
    @FXML
    private TextField playerNameField;
    @FXML
    private AnimatedButton refreshServersListButton;
    @FXML
    private ChoiceBox server_box;
    @FXML
    private GridPane grid;

    private CharacterPicker characterSelector = new CharacterPicker();

    /**
     * Default constructor
     */
    public JoinRoomMenu() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshServerInfo.setVisible(false);
        server_box.setVisible(false);
        SplitPane.setResizableWithParent(mainAnchorPane, false);
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);

        renderCharacterSelector();
        refreshServersList(null);
        refreshServersListButton.setOnAction(this::refreshServersList);
        joinServerButton.setOnAction(this::createAndLaunchClient);

    }

    private void refreshServersList(ActionEvent actionEvent) {
        Thread searchThread = new Thread(() -> {
            Platform.runLater(() -> {
                refreshServerInfo.setText("Searching servers on LAN...");
                refreshServerInfo.setVisible(true);
                server_box.setVisible(false);
            });

            SnackioNetClient cli = new SnackioNetClient();
            List<InetAddress> servers = cli.getAvailableServers();
            Platform.runLater(() -> server_box.getItems().setAll(servers));
            if (servers.size() > 0) {
                Platform.runLater(() -> {
                    server_box.setValue(server_box.getItems().get(0));
                    server_box.setVisible(true);
                    refreshServerInfo.setText("");
                    refreshServerInfo.setVisible(false);
                });


            } else {
                Platform.runLater(() -> {
                    server_box.setVisible(false);
                    refreshServerInfo.setText("No servers found.");
                });
            }
        });
        searchThread.start();
    }


    private void renderCharacterSelector() {
        grid.add(characterSelector, 1, 1);
    }

    private void createAndLaunchClient(ActionEvent actionEvent) {
        SnackioGame game = SnackioGame.getInstance();

        // Create the local player
        Player myPlayer;
        try {
            myPlayer = new Player(playerNameField.getText(), characterSelector.getSelectedCharacter());


            // Instantiate Network game engine to control gameplay
            AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer);

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

            cli.connectServer((InetAddress) server_box.getValue(), engine);

        } catch (UnhandledCharacterTypeException | NoCharacterSetException | UnhandledControllerException e) {
            e.printStackTrace();
        }

    }

}
