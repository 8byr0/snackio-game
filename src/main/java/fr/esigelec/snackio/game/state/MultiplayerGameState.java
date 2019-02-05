package fr.esigelec.snackio.game.state;

import fr.esigelec.snackio.core.GameMode;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.map.MapFactory;

import java.util.ArrayList;

public class MultiplayerGameState extends AbstractGameState {
    private ArrayList<Player> playersList = new ArrayList<>();
    private Player activePlayer;
    private String serverName;

    public MultiplayerGameState(){
        super();
    }

    public MultiplayerGameState(MapFactory.MapType type, String serverName) {
        super(type, GameMode.FIGHT_TO_DEATH);
        this.serverName = serverName;

        try {
            playersList.add(new Player("Xinyue", CharacterFactory.CharacterType.GOLDEN_KNIGHT));
            playersList.add(new Player("Hugues", CharacterFactory.CharacterType.BALD_MAN));
            activePlayer = new Player("Hugues", CharacterFactory.CharacterType.BALD_MAN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Player> getPlayers() {

        return this.playersList;
    }

    public String getServerName() {
        return serverName;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    @Override
    public String getName() {
        return serverName;
    }
}
