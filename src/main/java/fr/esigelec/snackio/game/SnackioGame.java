package fr.esigelec.snackio.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.pois.Bomb;
import fr.esigelec.snackio.game.pois.*;
import fr.esigelec.snackio.game.pois.bonuses.Cookie;
import fr.esigelec.snackio.game.pois.maluses.Freeze;
import fr.esigelec.snackio.game.pois.maluses.Paralysis;
import fr.esigelec.snackio.game.state.AbstractGameState;
import fr.esigelec.snackio.game.state.CoinQuestGameState;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.motion.IMotionController;
import fr.esigelec.snackio.game.map.Map;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.pois.bonuses.SpeedBonus;
import fr.esigelec.snackio.game.pois.listeners.PoiTriggeredListener;
import fr.esigelec.snackio.game.pois.maluses.SpeedMalus;

import java.util.ArrayList;
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

    private ArrayList<PoiTriggeredListener> poiTriggeredListeners = new ArrayList<>();
    private AbstractGameState gameState;
    public int lives = 3;

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

    }

    /**
     * Start the game (open game window)
     * To successfully execute this, you need to provide a Player and a Map
     */
    public void start(AbstractGameState gameState) throws GameCannotStartException {
        this.gameState = gameState;
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
        this.triggerPoiListeners(coin, null);
    }

    public void bombTouched(Bomb bomb, Character character) {
        System.out.println("You lost one life!");
        gameRenderer.removePointOfInterest(bomb);
        lives--;
        System.out.println("lives = " + lives);


    }

    public void freezeTouched(Freeze freeze, Character character) {
        System.out.println("You're frozen!");
        gameRenderer.removePointOfInterest(freeze);
    }

    public void cookieFound(Cookie cookie, Character character) {
        System.out.println("You get an extra life!");
        gameRenderer.removePointOfInterest(cookie);
        lives++;
        System.out.println("lives = " + lives);


    }

    public void getRandomItem(RandomItem randomItem, Character character) {
        gameRenderer.removePointOfInterest(randomItem);

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

    public void addPoiTriggeredListener(PoiTriggeredListener listener) {
        this.poiTriggeredListeners.add(listener);
    }

    private void triggerPoiListeners(iPoi poi, Player player) {
        for (PoiTriggeredListener listener : poiTriggeredListeners) {
            listener.poiTriggered(poi, player);
        }
    }

    public AbstractGameState getGameState() {
        return this.gameState;
    }

    public void setGameState(CoinQuestGameState gameState) {
        this.gameState = gameState;
    }

    public int getLives() {
        return lives;
    }
}
