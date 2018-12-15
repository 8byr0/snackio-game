package fr.esigelec.snackio.networking.experi;

import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.networking.Position;

import java.util.ArrayList;

// This class represents a player on the server.
public interface IPlayer {
    public void registerPlayer (Player localPlayer);


    public void setPosition(Position pos) throws NoCharacterSetException, UnhandledCharacterTypeException;

    void updatePlayerMotion(int id, Position position, Direction direction);

    void addRemotePlayer(Player existingPlayer);

    ArrayList<Player> getPlayers();
}