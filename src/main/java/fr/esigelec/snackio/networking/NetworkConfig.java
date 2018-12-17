package fr.esigelec.snackio.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import fr.esigelec.snackio.core.IGameEngine;
import fr.esigelec.snackio.core.models.INetPlayer;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.motion.KeyboardController;
import fr.esigelec.snackio.game.character.motion.NetworkController;
import fr.esigelec.snackio.game.character.texture.AnimatedCharacterSkin;
import fr.esigelec.snackio.game.pois.Coin;
import fr.esigelec.snackio.game.pois.PointOfInterest;
import fr.esigelec.snackio.game.pois.bonuses.SpeedBonus;
import fr.esigelec.snackio.game.pois.maluses.SpeedMalus;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import fr.esigelec.snackio.networking.server.SnackioNetServer;
import java.util.ArrayList;

/**
 * This class handles all network-related configurations.
 * It's in use by {@link SnackioNetClient} and {@link SnackioNetServer}
 */
public class NetworkConfig {
    static public final int udpPort = 54777;
    static public final int tcpPort = 54555;
    static public final int timeout = 5000;

    // These IDs are used to register objects in ObjectSpaces.
    static public final short RMI_PLAYER_ID = 1;
    static public final short RMI_GAME_ENGINE_ID = 2;

    /**
     * Register all classes required by both client and server to communicate together.
     *
     * @param endPoint one of SnackioNetClient or SnackioNetServer
     */
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        // This must be called in order to use ObjectSpaces.
        ObjectSpace.registerClasses(kryo);
        // The interfaces that will be used as remote objects must be registered.
        kryo.register(INetPlayer.class);
        kryo.register(Player.class);
        kryo.register(IGameEngine.class);
        kryo.register(Position.class);
        kryo.register(Character.class);
        kryo.register(Direction.class);
        kryo.register(PointOfInterest.class);
        kryo.register(Coin.class);
        kryo.register(SpeedBonus.class);
        kryo.register(SpeedMalus.class);
        kryo.register(AnimatedCharacterSkin.class);
        kryo.register(Character.CharacterStatus.class);
        kryo.register(Character.StepSound.class);
        kryo.register(KeyboardController.class);
        kryo.register(NetworkController.class);

        // GDX
        kryo.register(com.badlogic.gdx.graphics.Color.class);
        kryo.register(com.badlogic.gdx.maps.MapProperties.class);
        kryo.register(com.badlogic.gdx.utils.ObjectMap.class);

        // The classes of all method parameters and return values
        // for remote objects must also be registered.
        kryo.register(String[].class);
        kryo.register(ArrayList.class);
        kryo.register(java.nio.ByteBuffer.class);
        kryo.register(java.nio.Buffer.class);
        kryo.register(java.io.File.class);
        kryo.register(float[].class);
        kryo.register(float.class);
        kryo.register(java.util.HashMap.class);

    }
}