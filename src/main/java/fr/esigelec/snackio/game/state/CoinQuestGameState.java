package fr.esigelec.snackio.game.state;

import fr.esigelec.snackio.core.GameMode;
import fr.esigelec.snackio.game.map.MapFactory;

public class CoinQuestGameState extends AbstractGameState {
    private int fetchedCoins = 0;
    private int coinsToFetch;

    public CoinQuestGameState(MapFactory.MapType type, int coinsToFetch){
        super(type, GameMode.COINS_QUEST);
        this.coinsToFetch = coinsToFetch;
    }


    public int getFetchedCoins() {
        return fetchedCoins;
    }

    public void setFetchedCoins(int fetchedCoins) {
        this.fetchedCoins = fetchedCoins;
    }

    public int getCoinsToFetch() {
        return coinsToFetch;
    }

    public void setCoinsToFetch(int coinsToFetch) {
        this.coinsToFetch = coinsToFetch;
    }

    public void incrementFetchedPoints() {
        this.fetchedCoins += 1;
    }
}
