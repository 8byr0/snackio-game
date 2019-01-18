package fr.esigelec.snackio.ui;

import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.SoloGameEngine;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.character.texture.AnimatedCharacterSkin;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class JoinRoomMenu  implements Initializable {
    @FXML
    private Button join;
    @FXML
    private TextField playerName;
    @FXML
    private Button refresh;
    @FXML
    private ComboBox character;

    @FXML
    private ChoiceBox server_box;
    private Stage stage;


    public JoinRoomMenu() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        server("getInformation");
        character.getItems().setAll(CharacterFactory.CharacterType.values());
        refresh.setOnAction(this::refreshInfoServer);
        join.setOnAction(this::connection);

    }

    public void connection(ActionEvent actionEvent) {
        if(!playerName.getText().isEmpty() && !server_box.getSelectionModel().isEmpty() && !character.getSelectionModel().isEmpty()){
            server("getConnection");
        }
    }

    public void refreshInfoServer(ActionEvent actionEvent) {
            server("getInformation");

    }

    public void server (String info) {
        try {
            try {
                try {
                    SnackioGame game = SnackioGame.getInstance();
                    //        // Create the local player
                    Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.GOLDEN_KNIGHT);
                    if(info=="getConnection"){
                        myPlayer = new Player(playerName.getText(), CharacterFactory.CharacterType.valueOf(String.valueOf(character.getSelectionModel().getSelectedItem())));

                    }
                    //
                    //        /////////////// NETWORK CONTROL
                    //        // Instantiate Network game engine to control gameplay
                    AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer, MapFactory.MapType.DESERT_CASTLE);
                    //        // Instantiate a NetClient to exchange with client
                    SnackioNetClient cli = new SnackioNetClient(engine);
                    List<InetAddress> servers = cli.getAvailableServers();

                    //        logger.debug(servers);
                    if(servers.size() > 0) {
                        join.setDisable(false);
                        //Pour se connecter au server
                        if(info=="getConnection"){
                            System.out.println("connection");
                            cli.connectServer(servers.get(0));
                        }
                        //Pour recupérer les différents serveurs existants dans
                        if(info=="getInformation"){
                            System.out.println("information");
                            server_box.getItems().setAll(servers);
                        }
                    }else{
                        join.setDisable(true);
                        System.out.println("Aucun server");
                        server_box.getItems().clear();
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
