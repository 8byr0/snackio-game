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
        openJoinMenuButton.setOnMouseEntered(event -> {
            openJoinMenuButton.setTranslateX(1);
            openJoinMenuButton.setStyle("-fx-opacity: 1");
        });
        openJoinMenuButton.setOnMouseExited(event -> {
            openJoinMenuButton.setTranslateX(0);
            openJoinMenuButton.setStyle("-fx-opacity: 0.6");
        });
        openHostMenuButton.setOnMouseEntered(event -> {
            openHostMenuButton.setTranslateX(1);
            openHostMenuButton.setStyle("-fx-opacity: 1");
        });
        openHostMenuButton.setOnMouseExited(event -> {
            openHostMenuButton.setTranslateX(0);
            openHostMenuButton.setStyle("-fx-opacity: 0.6");
        });

    }

    public void openJoinMenu(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.JOIN_ROOM_MENU);

    }


    public void openHostMenu(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.SERVER_CONFIG_MENU);
    }
}
