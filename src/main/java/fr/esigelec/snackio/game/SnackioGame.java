package fr.esigelec.snackio.game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.esigelec.snackio.game.character.Character;

/**
 * High level Game class.
 */
public class SnackioGame  {
    private static SnackioGame instance = null;

    private GameRenderer gameRenderer = GameRenderer.getInstance();

    public static SnackioGame getInstance(){
        if(null == instance){
            instance = new SnackioGame();
        }
        return instance;
    }

    private SnackioGame(){
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        Lwjgl3Application app = new Lwjgl3Application(gameRenderer, config);
    }

    public void addCharacter(Character character){

    }
}
