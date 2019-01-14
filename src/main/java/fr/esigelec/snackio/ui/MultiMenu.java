package fr.esigelec.snackio.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MultiMenu implements Initializable {
    @FXML
    private Button openJoinMenuButton;

    @FXML
    private Button openHostMenuButton;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MAIN_MENU);
        openJoinMenuButton.setOnAction(this::openJoinMenu);
        openHostMenuButton.setOnAction(this::openHostMenu);

    }

    public void openJoinMenu(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MAIN_MENU);

    }


    public void openHostMenu(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.SERVER_CONFIG_MENU);
    }
}
