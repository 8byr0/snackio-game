package fr.esigelec.snackio.ui;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.esigelec.snackio.game.Animator;
import fr.esigelec.snackio.game.Drop;
import fr.esigelec.snackio.game.SnackioGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {
    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Button openSoloMenuButton;

    @FXML
    private Button openMultiMenuButton;

    private Stage stage;


    public MainMenu() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openMultiMenuButton.setOnAction(this::openMultiMenu);
        openSoloMenuButton.setOnAction(this::openSoloMenu);
    }



    public void openSoloMenu(ActionEvent actionEvent) {
        SnackioGame game = SnackioGame.getInstance();
    }

    public void openMultiMenu(ActionEvent actionEvent) {
        System.out.println("Open multiplayer menu");
        stage = (Stage) mainAnchorPane.getScene().getWindow();

        MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);
    }
}
