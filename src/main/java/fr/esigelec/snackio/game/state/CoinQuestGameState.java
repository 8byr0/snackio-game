package fr.esigelec.snackio.game.state;

import fr.esigelec.snackio.core.GameMode;
import fr.esigelec.snackio.game.map.MapFactory;

/**
 * This class Describes the Games State of a GameMode.COIN_QUEST
 */
public class CoinQuestGameState extends AbstractGameState {
    private int fetchedCoins = 0;
    private int coinsToFetch;

    /**
     * Default class constructor
     *
     * @param type         the type of map
     * @param coinsToFetch number of coins to fetch in this game
     */
    public CoinQuestGameState(MapFactory.MapType type, int coinsToFetch) {
        super(type, GameMode.COINS_QUEST);
        this.coinsToFetch = coinsToFetch;
    }

    /**
     * Get the number of fetched coins
     * TODO this method must be overrided in a NetworkCoinQuest class to add team reference
     * @return number of fetched coins by the player
     */
    public int getFetchedCoins() {
        return fetchedCoins;
    }

    /**
     * Set the number of fetched coins
     * @param fetchedCoins the number of fetched coins
     */
    public void setFetchedCoins(int fetchedCoins) {
        this.fetchedCoins = fetchedCoins;
    }

    /**
     * Get the number of coins to fetch
     * @return the number of coins to fetch
     */
    public int getCoinsToFetch() {
        return coinsToFetch;
    }

    /**
     * Set the number of coins to fetch
     * @param coinsToFetch the number of coins to fetch
     */
    public void setCoinsToFetch(int coinsToFetch) {
        this.coinsToFetch = coinsToFetch;
    }

    /**
     * Increment by 1 the number of fetched coins
     */
    public void incrementFetchedPoints() {
        this.fetchedCoins += 1;
    }

    @Override
    public String getName() {
        return "";
    }
}
