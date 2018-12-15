package fr.esigelec.snackio;

import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.models.IGameEngine;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.networking.client.SnackioNetClient;

public class SecondClient {
    public static void main(String[] args){
        SnackioGame game = SnackioGame.getInstance();

        // Create the local player
        Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.BALD_MAN);
        myPlayer.getCharacter().setPosition(100,800);

        /////////////// NETWORK CONTROL
        // Instantiate Network game engine to control gameplay
        IGameEngine engine = new NetworkGameEngine(game);
        // Instantiate a NetClient to exchange with client
        SnackioNetClient cli = new SnackioNetClient(engine, myPlayer);

        // Start the game with my player
        game.start(myPlayer);
    }
}
