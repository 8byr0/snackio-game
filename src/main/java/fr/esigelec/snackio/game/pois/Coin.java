package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl3.audio.Mp3;
import com.badlogic.gdx.backends.lwjgl3.audio.Wav;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.Character;

public class Coin extends AnimatedPointOfInterest {

    @Override
    public void execute(Character character) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/coin.wav"));

        music.play();
        SnackioGame.getInstance().coinFound(this, character);
    }
}
