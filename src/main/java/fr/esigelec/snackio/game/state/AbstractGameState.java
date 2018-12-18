package fr.esigelec.snackio.game.state;

import fr.esigelec.snackio.core.GameMode;
import fr.esigelec.snackio.game.map.MapFactory;

/**
 * Abstract game state is a class that must be inherited to describe the state of a running / about to run game.
 */
public abstract class AbstractGameState {
    protected MapFactory.MapType mapType;
    protected GameMode mode;

    /**
     * Class Constructor
     * @param type The map type
     * @param mode the chosen Game Mode
     */
    AbstractGameState(MapFactory.MapType type, GameMode mode){
        this.mapType = type;
        this.mode = mode;
    }

    /**
     * Get the type of map in use
     * @return MapType the type of the map
     */
    public MapFactory.MapType getMapType() {
        return mapType;
    }

    /**
     * Set the map type to use
     * @param mapType the map type
     */
    public void setMapType(MapFactory.MapType mapType) {
        this.mapType = mapType;
    }

    /**
     * Get actual game mode
     * @return GameMode
     */
    public GameMode getGameMode() {
        return mode;
    }

    /**
     * Set the game mode
     * @param mode mode
     */
    public void setGameMode(GameMode mode) {
        this.mode = mode;
    }
}
