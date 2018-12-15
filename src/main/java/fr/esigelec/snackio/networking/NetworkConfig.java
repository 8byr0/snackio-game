package fr.esigelec.snackio.networking;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.IndexArray;
import com.badlogic.gdx.math.Matrix4;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import fr.esigelec.snackio.core.models.IGameEngine;
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
import fr.esigelec.snackio.networking.experi.IChatFrame;
import fr.esigelec.snackio.networking.experi.IPlayer;

import java.util.ArrayList;

public class NetworkConfig {
    static public final int port = 54777;

    // These IDs are used to register objects in ObjectSpaces.
    static public final short RMI_PLAYER_ID = 1;
    static public final short RMI_GAME_ENGINE_ID = 2;

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        // This must be called in order to use ObjectSpaces.
        ObjectSpace.registerClasses(kryo);
        // The interfaces that will be used as remote objects must be registered.
        kryo.register(IPlayer.class);
        kryo.register(Player.class);
        kryo.register(IGameEngine.class);
        kryo.register(IChatFrame.class);
        kryo.register(Position.class);
        kryo.register(fr.esigelec.snackio.core.models.Player.class);
        kryo.register(Character.class);
        kryo.register(Direction.class);
        kryo.register(PointOfInterest.class);
        kryo.register(Coin.class);
        kryo.register(SpeedBonus.class);
        kryo.register(SpeedMalus.class);
        kryo.register(AnimatedCharacterSkin.class);
        kryo.register(Character.CharacterStatus.class);

        // GDX
        kryo.register(Character.StepSound.class);
        kryo.register(KeyboardController.class);
        kryo.register(NetworkController.class);
        kryo.register(Files.class);
        kryo.register(Files.FileType.class);
        kryo.register(Pixmap.class);
        kryo.register(Pixmap.Filter.class);
        kryo.register(Pixmap.Format.class);
        kryo.register(Texture.TextureFilter.class);
        kryo.register(Texture.TextureWrap.class);
        kryo.register(SpriteBatch.class);
        kryo.register(Color.class);
        kryo.register(Matrix4.class);
        kryo.register(Animation[].class);
        kryo.register(Animation.class);
        kryo.register(Animation.PlayMode.class);
        kryo.register(TextureRegion[].class);
        kryo.register(TextureRegion.class);
        kryo.register(Texture.class);
        kryo.register(Texture[].class);
        kryo.register(FileTextureData.class);
        kryo.register(Lwjgl3FileHandle.class);
        kryo.register(Mesh.class);
        kryo.register(IndexArray.class);
        kryo.register(com.badlogic.gdx.graphics.OrthographicCamera.class);
        kryo.register(com.badlogic.gdx.graphics.Camera.class);
        kryo.register(com.badlogic.gdx.graphics.GL20.class);


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