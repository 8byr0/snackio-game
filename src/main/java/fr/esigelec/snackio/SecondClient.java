package fr.esigelec.snackio;

import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.IGameEngine;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.networking.client.SnackioNetClient;

import java.net.InetAddress;
import java.util.List;

/**
 * THIS CLASS IS ONLY TO DEBUG USING MULTIPLE CLIENTS
 */
public class SecondClient {
    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws GameCannotStartException, UnhandledCharacterTypeException, NoCharacterSetException, UnhandledControllerException {
        SnackioGame game = SnackioGame.getInstance();

        // Create the local player
        Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.BALD_MAN);
        myPlayer.getCharacter().setPosition(100,800);

        /////////////// NETWORK CONTROL
        // Instantiate Network game engine to control gameplay
        IGameEngine engine = new NetworkGameEngine(game, myPlayer);
        // Instantiate a NetClient to exchange with client
        SnackioNetClient cli = new SnackioNetClient(engine);
        List<InetAddress> servers = cli.getAvailableServers();
        System.out.println(servers);

        if(servers.size() > 0) {
            cli.connectServer(servers.get(0));
        }
//        engine.startGame();
        game.start();
    }
}
