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
import java.util.List;

/**
 * THIS CLASS IS ONLY TO DEBUG USING MULTIPLE CLIENTS
 */
public class ThirdClient {
    public static void main(String[] args) throws GameCannotStartException, UnhandledCharacterTypeException, NoCharacterSetException, UnhandledControllerException {
//        SnackioGame game = SnackioGame.getInstance();
//
//        // Create the local player
//        Player myPlayer = new Player("Bob", CharacterFactory.CharacterType.INSPECTOR);
//        myPlayer.getCharacter().setPosition(100,900);
//
//        /////////////// NETWORK CONTROL
//        // Instantiate Network game engine to control gameplay
//        AbstractGameEngine engine = new SoloGameEngine(game, myPlayer, MapFactory.MapType.DESERT_CASTLE, 5);
//        // Instantiate a NetClient to exchange with client
//
//        engine.startGame();


        // Create a Game

        SnackioGame game = SnackioGame.getInstance();

        // Create the local player
        Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.GOLDEN_KNIGHT);

        MultiplayerGameState gameState = new MultiplayerGameState(MapFactory.MapType.DESERT_CASTLE, "YOLO");
        // Instantiate Network game engine to control gameplay
        AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer, gameState);
        // Instantiate a NetClient to exchange with client
        SnackioNetClient cli = new SnackioNetClient(engine);
        List<InetAddress> servers = cli.getAvailableServers();

        if(servers.size() > 0) {
            cli.connectServer(servers.get(0));
        }
        engine.startGame();
    }
}