package fr.esigelec.snackio.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerConfigMenu implements Initializable {
    @FXML
    private TextField roomName;

    @FXML
    private Button submit;

    @FXML
    private ChoiceBox map;

    @FXML
    private ChoiceBox mode;

    private Stage stage;

    public ServerConfigMenu() {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //map.setItems(FXCollections.observableArrayList("1", "2" , "3"));
        //mode.setItems(FXCollections.observableArrayList("1", "2" , "3"));
        //submit.setOnAction(this::submitServer);
    }

    public void submitServer(ActionEvent actionEvent) {
        //roomName.getText();
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);
    }
}
