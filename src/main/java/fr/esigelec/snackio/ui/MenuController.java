package fr.esigelec.snackio.ui;

import com.esotericsoftware.minlog.Log;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MenuController {
    private Stage primaryStage;

    public enum Menus {
        MAIN_MENU,
        SOLO_MENU,
        MULTI_MENU,
        SERVER_CONFIG_MENU,
        JOIN_ROOM_MENU,
        SOLO_CONFIG_MENU
    }
    public static Stage stage;

    public static Stage getStage(){
        return stage;
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
        this.primaryStage.setTitle("snackio");
    }

    public void openMenu(Menus menu) {
        String pathToMenuFile = null;
        switch (menu) {
            case MAIN_MENU:
                Snippet.back("YES");
                pathToMenuFile = "/menus/MainMenu.fxml";
                break;
            case SOLO_CONFIG_MENU:
                Snippet.back("NO");
                pathToMenuFile = "/menus/SoloConfigMenu.fxml";
                break;
            case MULTI_MENU:
                Snippet.back("NO");
                pathToMenuFile = "/menus/MultiMenu.fxml";
                break;
            case SOLO_MENU:
                pathToMenuFile = "/menus/SoloMenu.fxml";
                break;
            case SERVER_CONFIG_MENU:
                pathToMenuFile = "/menus/ServerConfigMenu.fxml";
                break;
            case JOIN_ROOM_MENU:
                pathToMenuFile = "/menus/JoinRoomMenu.fxml";
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

        scn.getStylesheets().add(getClass().getResource("/CSS/menus.css").toExternalForm());
        primaryStage.setScene(scn);
        primaryStage.setResizable(false);
        stage=primaryStage;
        primaryStage.show();
    }
}
