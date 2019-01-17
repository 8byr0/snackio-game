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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class JoinRoomMenu implements Initializable {
    @FXML public ChoiceBox server_box;
    @FXML public TextField testing;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
                try {
                    try {
                        //System.out.println(e.getAvailableServers());
                        SnackioGame game = SnackioGame.getInstance();
                //
                //        // Create the local player
                        Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.GOLDEN_KNIGHT);
                //
                //        /////////////// NETWORK CONTROL
                //        // Instantiate Network game engine to control gameplay
                        AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer, MapFactory.MapType.DESERT_CASTLE);
                //        // Instantiate a NetClient to exchange with client
                        SnackioNetClient cli = new SnackioNetClient(engine);
                        List<InetAddress> servers = cli.getAvailableServers();

                //        logger.debug(servers);
                        if(servers.size() > 0) {
                            cli.connectServer(servers.get(0));
                        }
                        System.out.println("youkhou"+servers.get(0));
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
    public static void info(List list){
    }
}
