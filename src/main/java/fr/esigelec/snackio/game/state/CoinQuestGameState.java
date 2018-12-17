package fr.esigelec.snackio.game.state;

import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.state.AbstractGameState;

public class CoinQuestGameState extends AbstractGameState {
    private int fetchedCoins = 0;
    private int coinsToFetch;

    public CoinQuestGameState(int coinsToFetch){
        this.coinsToFetch = coinsToFetch;
    }

    public MapFactory.MapType getMapType() {
        return mapType;
    }

    public void setMapType(MapFactory.MapType mapType) {
        this.mapType = mapType;
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
