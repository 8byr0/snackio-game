package fr.esigelec.snackio;

import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.models.IGameEngine;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Snackio extends Application {
    private static final Logger logger = LogManager.getLogger(Snackio.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Snackio Game started");

        logger.info("Initialisation started...");
        logger.info("Fetching menu controller");
//        MenuController controller = MenuController.getInstance(primaryStage);

        logger.info("Opening main menu");
//        controller.openMenu(MenuController.Menus.MAIN_MENU);

        logger.info("...Initialisation finished");


        // Create a Game
        SnackioGame game = SnackioGame.getInstance();

        // Create the local player
        Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.GOLDEN_KNIGHT);

        /////////////// NETWORK CONTROL
        // Instantiate Network game engine to control gameplay
        IGameEngine engine = new NetworkGameEngine(game);
        // Instantiate a NetClient to exchange with client
//        SnackioNetClient cli = new SnackioNetClient(engine, myPlayer);

        // Start the game with my player
        game.start(myPlayer);
    }

}
