package fr.esigelec.snackio.ui;

import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import fr.esigelec.snackio.ui.customToggles.CharacterPicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.InetAddress;
import java.net.URL;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class JoinRoomMenu implements Initializable {
    private static final int SQUARE_SIDE = 67;
    @FXML
    private SplitPane alfa;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button join;
    @FXML
    private TextField playerName;
    @FXML
    private Button refresh;

    private CharacterPicker characterSelector = new CharacterPicker();

    //    private ToggleGroup characterGroup = new ToggleGroup();

    @FXML
    private ChoiceBox server_box;

    private ToggleButton perChoice;

    @FXML
    private GridPane grid;


    public JoinRoomMenu() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alfa.setResizableWithParent(mainAnchorPane,false);
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        server("getInformation");
        renderCharacterSelector();

        join.setOnMouseEntered(event -> {
            if(!playerName.getText().isEmpty() && !server_box.getSelectionModel().isEmpty() && characterGroup.getSelectedToggle() != null) {
                join.setTranslateX(1);
                join.setStyle("-fx-opacity: 1");
                join.setOnAction(this::connection);
            }
        });
        join.setOnMouseExited(event -> {
            join.setStyle("-fx-opacity: 0.6");
            join.setTranslateX(0);
        });

        refresh.setOnMouseEntered(event -> {
            refresh.setTranslateX(1);
            refresh.setStyle("-fx-opacity: 1");
        });
        refresh.setOnMouseExited(event -> {
            refresh.setStyle("-fx-opacity: 0.6");
            refresh.setTranslateX(0);
        });

        refresh.setOnAction(this::refreshInfoServer);

    }

    private void connection(ActionEvent actionEvent) {
        if (!playerName.getText().isEmpty() && !server_box.getSelectionModel().isEmpty() && characterSelector.getSelectedToggle() != null) {
            server("getConnection");
        }
    }

    private void refreshInfoServer(ActionEvent actionEvent) {
        server("getInformation");
    }

    private void renderCharacterSelector() {
        grid.add(characterSelector, 1, 1);
    }

    public void server(String info) {
        try {
            SnackioGame game = SnackioGame.getInstance();
            // Create the local player
            Player myPlayer = new Player("", CharacterFactory.CharacterType.GOLDEN_KNIGHT);
            if (info == "getConnection") {
                myPlayer = new Player(playerName.getText(), characterSelector.getSelectedCharacter());
            }
            // TODO fetch map from server
            AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer, MapFactory.MapType.DESERT_CASTLE);

            SnackioNetClient cli = new SnackioNetClient(engine);
            List<InetAddress> servers = cli.getAvailableServers();

            if (servers.size() > 0) {
                join.setDisable(false);
                //Pour se connecter au server
                if (info == "getConnection") {
                    System.out.println("connection");
                    cli.connectServer(servers.get(0));
                }
                //Pour recupérer les différents serveurs existants dans
                if (info == "getInformation") {
                    System.out.println("information");
                    server_box.getItems().setAll(servers);
                }
            } else {
                join.setDisable(true);
                System.out.println("Aucun server");
                server_box.getItems().clear();
            }
        } catch (NoCharacterSetException | UnhandledCharacterTypeException | UnhandledControllerException e) {
            Log.error(e.getMessage(), e);
        }
    }
}
