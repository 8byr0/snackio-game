package fr.esigelec.snackio;

import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.SoloGameEngine;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.state.AbstractGameState;
import fr.esigelec.snackio.game.state.MultiplayerGameState;
import fr.esigelec.snackio.networking.client.SnackioNetClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * THIS CLASS IS ONLY TO DEBUG USING MULTIPLE CLIENTS
 */
public class SnackioDebugClient {
    public static void main(String[] args) throws GameCannotStartException, UnhandledCharacterTypeException, NoCharacterSetException, UnhandledControllerException {
        SnackioGame game = SnackioGame.getInstance();

        // Create the local player
        Player myPlayer;
        try {
            myPlayer = new Player("HUUU", CharacterFactory.CharacterType.INDIANA);


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

            cli.connectServer(InetAddress.getByName("127.0.0.1"), engine);

        } catch (UnhandledCharacterTypeException | NoCharacterSetException | UnhandledControllerException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}