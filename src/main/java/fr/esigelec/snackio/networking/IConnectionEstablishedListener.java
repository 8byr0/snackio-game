package fr.esigelec.snackio.networking;

import fr.esigelec.snackio.game.state.AbstractGameState;

public interface IConnectionEstablishedListener {
    void onSuccess(AbstractGameState gameState);
}
