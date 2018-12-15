package fr.esigelec.snackio;

import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.IGameEngine;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.networking.client.SnackioNetClient;

public class SecondClient {
    public static void main(String[] args) throws GameCannotStartException {
        SnackioGame game = SnackioGame.getInstance();

        // Create the local player
        Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.BALD_MAN);
        myPlayer.getCharacter().setPosition(100,800);

        /////////////// NETWORK CONTROL
        // Instantiate Network game engine to control gameplay
        IGameEngine engine = new NetworkGameEngine(game, myPlayer);
        // Instantiate a NetClient to exchange with client
        SnackioNetClient cli = new SnackioNetClient(engine);

        // Start the game with my player
        game.start();
    }
}
