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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;

public class ServerConfigMenu implements Initializable {

    @FXML
    private TextField playerName;

    @FXML
    private Button submit;

    @FXML
    private ComboBox map;

    @FXML
    private ComboBox character;
    @FXML
    private GridPane grid;

    public ServerConfigMenu() {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        List<Enum> somethingList = Arrays.asList(CharacterFactory.CharacterType.values());
        map.getItems().setAll(MapFactory.MapType.values());
        character.getItems().setAll(CharacterFactory.CharacterType.values());
        submit.setOnAction(this::submitServer);
    }

    public void submitServer(ActionEvent actionEvent) {
        if(!map.getSelectionModel().isEmpty() && !character.getSelectionModel().isEmpty() && !playerName.getText().isEmpty()){
            try {
                try {
                    try {
                        try{
                            try{
                        SnackioGame game = SnackioGame.getInstance();
                        //        // Create the local player
                        Player myPlayer = new Player(playerName.getText(), CharacterFactory.CharacterType.valueOf(String.valueOf(character.getSelectionModel().getSelectedItem())));

                        //
                        //        /////////////// NETWORK CONTROL
                        //        // Instantiate Network game engine to control gameplay
                        AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer, MapFactory.MapType.valueOf(String.valueOf(map.getSelectionModel().getSelectedItem())));
                        //        // Instantiate a NetClient to exchange with client
                        SnackioNetClient cli = new SnackioNetClient(engine);
                        NetworkGameEngine nEngine=new NetworkGameEngine(game,myPlayer,MapFactory.MapType.valueOf(String.valueOf(map.getSelectionModel().getSelectedItem())));
                        List<InetAddress> servers = cli.getAvailableServers();
                        String[] difficultWords = new String[10];
                        difficultWords[0]="you";
                        SnackioNetServer.main(difficultWords);
                        //cli.connectServer(servers.get(0));
                        engine.startGame();
                        //000000000
                        } catch (IOException e) {
                            Log.error(e.getMessage(), e);
                        }
                        } catch (GameCannotStartException e) {
                            Log.error(e.getMessage(), e);
                        }

                    } catch (NoCharacterSetException e) {
                        Log.error(e.getMessage(), e);
                    }

                } catch (UnhandledControllerException e) {
                    Log.error(e.getMessage(), e);
                }
            } catch (
                    UnhandledCharacterTypeException e) {
                Log.error(e.getMessage(), e);
            }
        }
    }
}

