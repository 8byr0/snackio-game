package fr.esigelec.snackio.core;

import fr.esigelec.snackio.core.models.IGameEngine;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.listeners.PlayerAddedListener;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.networking.Position;

import java.util.ArrayList;

public class NetworkGameEngine implements IGameEngine {
    SnackioGame game;
    ArrayList<PlayerAddedListener> playerAddedListeners = new ArrayList<>();

    public NetworkGameEngine(SnackioGame game) {
        this.game = game;
    }

    @Override
    public void addPointOfInterest(iPoi poi) {
        System.out.println("Server asked to add a new poi");
        game.addPointOfInterest(poi);
    }

    @Override
    public void removePointOfInterest(iPoi poi) {
        game.removePointOfInterest(poi);
    }

    @Override
    public Player getPlayer() {
        return game.getPlayer();
    }

    @Override
    public void addPlayerAddedListener(PlayerAddedListener o) {
        this.playerAddedListeners.add(o);
    }

    private void triggerPlayerAddedListeners(Player player) {
        for (PlayerAddedListener listener : playerAddedListeners) {
            listener.playerAdded(player);
        }
    }

    @Override
    public void addPlayer(Player player) {
        game.addPlayer(player, false);
        triggerPlayerAddedListeners(player);
    }

    @Override
    public void updatePlayerPosition(int id, Position position, Direction direction) {
//        System.out.println("Update player");
        Player player = this.game.getPlayer(id);
        if (null != player) {
            player.setMoving(true);
            player.setPosition(position);
            player.setDirection(direction);
            player.setMoving(false);
        }
    }
}
