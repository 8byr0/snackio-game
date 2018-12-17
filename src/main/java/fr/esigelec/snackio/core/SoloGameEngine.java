package fr.esigelec.snackio.core;

import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.core.models.IRMIExecutablePlayer;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.listeners.PlayerAddedListener;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.pois.Coin;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.networking.Position;

import java.util.ArrayList;

public class SoloGameEngine extends AbstractGameEngine {

    private ArrayList<Coin> coins = new ArrayList<>();

    /**
     * Default constructor
     *
     * @param game snackioGame that will be managed by this engine
     */
    public SoloGameEngine(SnackioGame game, Player player, MapFactory.MapType type) throws NoCharacterSetException, UnhandledControllerException {
        super(game,player,type);
    }


    /**
     * Call this method when the Engine is properly configured
     */
    @Override
    public void startGame() throws GameCannotStartException {
        super.startGame();
    }

}
