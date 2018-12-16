package fr.esigelec.snackio.ui;

import fr.esigelec.snackio.game.SnackioGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu  implements Initializable {

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
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);
    }

}
