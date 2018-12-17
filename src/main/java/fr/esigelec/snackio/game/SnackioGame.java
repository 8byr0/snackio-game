package fr.esigelec.snackio.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.motion.IMotionController;
import fr.esigelec.snackio.game.map.Map;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.pois.Coin;
import fr.esigelec.snackio.game.pois.PointOfInterest;
import fr.esigelec.snackio.game.pois.bonuses.SpeedBonus;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.game.pois.maluses.SpeedMalus;

import java.util.HashMap;

/**
 * High level Game class.
 * This is in charge of interacting with GameRenderer instance to add high-level elements
 * to the Game(Player, Map)...
 */
public class SnackioGame {
    // SINGLETON
    private static SnackioGame instance = null;

    // GAME RENDERING
    private GameRenderer gameRenderer = GameRenderer.getInstance();

    // PLAYERS
    private Player defaultPlayer;
    private HashMap<Integer, Player> playersHashmap = new HashMap<>();


    /**
     * Singleton implementation
     *
     * @return existing instance or a new one if not exists
     */
    public static SnackioGame getInstance() {
        if (null == instance) {
            instance = new SnackioGame();
        }
        return instance;
    }

    /**
     * Private Constructor that's called by singleton method
     */
    private SnackioGame() {
        // Add a few bonuses / maluses
        // TODO remove this from here
        PointOfInterest speedBonus = new SpeedBonus();
        speedBonus.setPosition(800, 800);
        addPointOfInterest(speedBonus);

        PointOfInterest speedMalus = new SpeedMalus();
        speedMalus.setPosition(900, 900);
        addPointOfInterest(speedMalus);

        Coin testCoin = new Coin();
        testCoin.setPosition(750, 200);
        addPointOfInterest(testCoin);
    }

    /**
     * Start the game (open game window)
     * To successfully execute this, you need to provide a Player and a Map
     */
    public void start() throws GameCannotStartException {
        if (null == defaultPlayer || null == gameRenderer) {
            throw new GameCannotStartException("Game could not start, Have you instantiated an engine?");
        }
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        Lwjgl3Application app = new Lwjgl3Application(gameRenderer, config);
    }

    /**
     * Method to call when a Character fetches a coin
     *
     * @param coin      coin reference
     * @param character Character who found the coin
     *                  TODO make sure this is useful
     */
    public void coinFound(Coin coin, Character character) {
        System.out.println("YEAH, coin found!");
        gameRenderer.removePointOfInterest(coin);
    }

    /**
     * Add a point of interest to the game
     *
     * @param poi the point of interest to add to the Map
     */
    public void addPointOfInterest(iPoi poi) {
        gameRenderer.addPointOfInterest(poi);
    }

    /**
     * Remove a point of interest from the game
     *
     * @param poi the point of interest to remove from the Map
     */
    public void removePointOfInterest(iPoi poi) {
        gameRenderer.removePointOfInterest(poi);
    }

    /**
     * Add a Player to the Game
     * If active, it will be kept as the main one
     *
     * @param player the Player to add
     * @param active if true, the Character will be followed by the main Camera
     */
    public void addPlayer(Player player, boolean active) throws UnhandledControllerException, NoCharacterSetException {
        if (active) {
            this.defaultPlayer = player;
            defaultPlayer.setMotionController(IMotionController.KEYBOARD);
        } else {
            player.setMotionController(IMotionController.NETWORK);
        }

        gameRenderer.addCharacter(player.getCharacter(), active);

        this.playersHashmap.put(player.getID(), player);
    }

    /**
     * Get the active player of this Game
     *
     * @return the Player added with active=true
     */
    public Player getPlayer() {
        if (null == defaultPlayer) {
            System.out.println("DEFAULT PLAYER IS NULL");
        }
        return defaultPlayer;
    }

    /**
     * Get a Player of this Game identified by its ID
     *
     * @param id ID of the Player
     * @return the Player instance
     */
    public Player getPlayer(int id) {
        // TODO make sure player exists
        if (!this.playersHashmap.containsKey(id)) {
            System.out.println("ERROR PLAYER DOES NOT EXIST");
        }
        return this.playersHashmap.get(id);
    }

    /**
     * Set the tiled map
     *
     * @param map the map to set
     */
    public void setMap(MapFactory.MapType map) {
        Map theMap = MapFactory.getMap(map);
        this.gameRenderer.setSnackioMap(theMap);
    }

    public void removePlayer(int playerID) {
        Player playerToRemove = this.playersHashmap.get(playerID);
        try {
            this.gameRenderer.removeCharacter(playerToRemove.getCharacter());
        } catch (NoCharacterSetException e) {
            e.printStackTrace();
        }
        this.playersHashmap.remove(playerID);
    }
}
