package fr.esigelec.snackio.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MultiMenu implements Initializable {

    @FXML
    private AnchorPane multiAnchorPane;

    @FXML
    private Button joinRoom;

    @FXML
    private Button hostRoom;

    private Stage stage;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        joinRoom.setOnAction(this::openMulMenu);
        hostRoom.setOnAction(this::openServerConfig);
    }

    public void openMulMenu(ActionEvent actionEvent) {
        //stage = (Stage) multiAnchorPane.getScene().getWindow();
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MAIN_MENU);
    }

    public void openServerConfig (ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.SERVER_CONFIG_MENU);
    }
}
