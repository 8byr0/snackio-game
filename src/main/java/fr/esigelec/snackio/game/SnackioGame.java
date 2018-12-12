package fr.esigelec.snackio.game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * High level Game class.
 */
public class SnackioGame  {
    private static SnackioGame instance = null;
    private SnackioMap the_map;
    private GameRenderer gameRenderer = new GameRenderer();

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
