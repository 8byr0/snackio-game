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
        alfa.setResizableWithParent(mainAnchorPane, false);
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        server("getInformation");
        renderCharacterSelector();
        refreshServersList(null);
        refreshServersListButton.setOnAction(this::refreshServersList);
        joinServerButton.setOnAction(this::createAndLaunchClient);

    }

    private void connection(ActionEvent actionEvent) {
        if (!playerNameField.getText().isEmpty() && !server_box.getSelectionModel().isEmpty() && characterSelector.getSelectedToggle() != null) {
            server("getConnection");
        }
    }

    private void refreshServersList(ActionEvent actionEvent) {
//        server("getInformation");

        Thread searchThread = new Thread(() -> {
            Platform.runLater(() -> {
                refreshServerInfo.setText("Searching servers on LAN...");
                refreshServerInfo.setVisible(true);
                server_box.setVisible(false);
            });

            SnackioNetClient cli = new SnackioNetClient();
            List<InetAddress> servers = cli.getAvailableServers();
            Platform.runLater(() -> {
                server_box.getItems().setAll(servers);
            });
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

    public void createAndLaunchClient(ActionEvent actionEvent) {
        SnackioGame game = SnackioGame.getInstance();

        // Create the local player
        Player myPlayer = null;
        try {
            myPlayer = new Player(playerNameField.getText(), characterSelector.getSelectedCharacter());

            MultiplayerGameState gameState = new MultiplayerGameState(MapFactory.MapType.DESERT_CASTLE, "YOLO");

            // Instantiate Network game engine to control gameplay
            AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer, gameState);

            // Instantiate a NetClient to exchange with client
            SnackioNetClient cli = new SnackioNetClient();


            cli.connectServer((InetAddress) server_box.getValue(), engine);
            engine.startGame();

        } catch (UnhandledCharacterTypeException | GameCannotStartException | NoCharacterSetException | UnhandledControllerException e) {
            e.printStackTrace();
        }

    }

    public void server(String info) {
        try {
            SnackioGame game = SnackioGame.getInstance();
            // Create the local player
            Player myPlayer = new Player("", CharacterFactory.CharacterType.GOLDEN_KNIGHT);
            if (info == "getConnection") {
                myPlayer = new Player(playerNameField.getText(), characterSelector.getSelectedCharacter());
            }

            // TODO fetch map from server
//            AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer, MapFactory.MapType.DESERT_CASTLE);

//            SnackioNetClient cli = new SnackioNetClient(engine);
//            List<InetAddress> servers = cli.getAvailableServers();

//            if (servers.size() > 0) {
//                joinServerButton.setDisable(false);
//                //Pour se connecter au server
//                if (info == "getConnection") {
//                    System.out.println("connection");
//                    cli.connectServer(servers.get(0));
//                }
//                //Pour recupérer les différents serveurs existants dans
//                if (info == "getInformation") {
//                    System.out.println("information");
//                    server_box.getItems().setAll(servers);
//                }
//            } else {
//                joinServerButton.setDisable(true);
//                System.out.println("Aucun server");
//                server_box.getItems().clear();
//            }
        } catch (UnhandledCharacterTypeException e) {
            Log.error(e.getMessage(), e);
        }
    }

}
