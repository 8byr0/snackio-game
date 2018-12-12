package fr.esigelec.snackio.ui;

import com.esotericsoftware.minlog.Log;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    private Stage primaryStage;

    public enum Menus {
        MAIN_MENU,
        SOLO_MENU,
        MULTI_MENU,
        SERVER_CONFIG_MENU
    }

    private static MenuController instance;

    public static MenuController getInstance(Stage primaryStage) {
        if (null == instance) {
            instance = new MenuController(primaryStage);
        }
        return instance;
    }

    private MenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void openMenu(Menus menu) {
        String pathToMenuFile = null;
        switch (menu) {
            case MAIN_MENU:
                pathToMenuFile = "/menus/MainMenu.fxml";
                break;
            case MULTI_MENU:
                pathToMenuFile = "/menus/MultiMenu.fxml";
                break;
            case SOLO_MENU:
                pathToMenuFile = "/menus/SoloMenu.fxml";
                break;
            case SERVER_CONFIG_MENU:
                pathToMenuFile = "/menus/ServerConfigMenu.fxml";
                break;
            default:
                Log.warn("Requested menu does not exist. Cannot proceed.");
                throw new IllegalStateException("Requested menu does not exist. Cannot proceed.");

        }

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(pathToMenuFile));

        } catch (IOException e) {
            Log.error(e.getMessage(), e);
        }

        assert root != null;
        Scene scn = new Scene(root, 700, 500);

        // Uncomment this line when debug ended
        // primaryStage.setFullScreen(true);
//        primaryStage.setMaximized(true);
//        primaryStage.setMinWidth(700);
//        primaryStage.setMinHeight(500);
        primaryStage.setScene(scn);
        primaryStage.show();
    }
}
