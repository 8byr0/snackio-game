package fr.esigelec.snackio.game.state;

import fr.esigelec.snackio.core.GameMode;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.game.map.MapFactory;

import java.util.ArrayList;

public class MultiplayerGameState extends AbstractGameState {
    private ArrayList<Player> playersList;
    private Player activePlayer;

    public MultiplayerGameState(MapFactory.MapType type) {
        super(type, GameMode.FIGHT_TO_DEATH);

    }

    public ArrayList<Player> getPlayers(){
        return this.playersList;
    }

    public String getRoomName(){
        return "TEST ROOM";
    }

    public Player getActivePlayer(){
        return activePlayer;
    }
}
