package fr.esigelec.snackio;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.ui.MenuController;
import javafx.application.Application;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point of the full software
 */
public class Snackio extends Application {
    private static final Logger logger = LogManager.getLogger(Snackio.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws GameCannotStartException, UnhandledCharacterTypeException, NoCharacterSetException, UnhandledControllerException {
        logger.info("Snackio Game started");

        logger.info("Initialisation started...");
        logger.info("Fetching menu controller");
        MenuController controller = MenuController.getInstance(primaryStage);

        logger.info("Opening main menu");
        controller.openMenu(MenuController.Menus.MAIN_MENU);

        logger.info("...Initialisation finished");

        SplitPane ser=new SplitPane();

//        // Create a Game
//
//        SnackioGame game = SnackioGame.getInstance();
//
//        // Create the local player
//        Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.GOLDEN_KNIGHT);
//
//        /////////////// NETWORK CONTROL
//        // Instantiate Network game engine to control gameplay
//        AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer, MapFactory.MapType.DESERT_CASTLE);
//        // Instantiate a NetClient to exchange with client
//        SnackioNetClient cli = new SnackioNetClient(engine);
//        List<InetAddress> servers = cli.getAvailableServers();
//        logger.debug(servers);
//
//        if(servers.size() > 0) {
//            cli.connectServer(servers.get(0));
//        }
//        engine.startGame();

    }

}
