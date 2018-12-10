package fr.esigelec.snackio;

import fr.esigelec.snackio.ui.MenuController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Snackio extends Application {
    public static final Logger logger = LogManager.getLogger(Snackio.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Snackio Game started");

        logger.info("Initialisation started...");
        logger.info("Fetching menu controller");
        MenuController controller = MenuController.getInstance(primaryStage);

        logger.info("Opening main menu");
        controller.openMenu(MenuController.Menus.MAIN_MENU);

        logger.info("...Initialisation finished");
    }

}
