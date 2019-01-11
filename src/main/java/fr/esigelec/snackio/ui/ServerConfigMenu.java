package fr.esigelec.snackio.ui;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerConfigMenu implements Initializable {

    @FXML
    private TextField roomName;

    @FXML
    private Button submit;

    @FXML
    private ComboBox map;

    @FXML
    private ComboBox mode;

    private Stage stage;
    private String quest;
    private Scene scene;

    @FXML
    private GridPane grid;

    public ServerConfigMenu() {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        ObservableList<String> options = FXCollections.observableArrayList("option 1", "option 2" , "option 3");
        map = new ComboBox();
        mode = new ComboBox(options);
        map.getItems().addAll(
                "jacob.smith@example.com",
                "isabella.johnson@example.com",
                "ethan.williams@example.com",
                "emma.jones@example.com",
                "michael.brown@example.com"
        );

        grid.add(map, 1, 1);
        grid.add(mode, 1, 4);
        submit.setOnAction(this::submitServer);
    }

    public void submitServer(ActionEvent actionEvent) {
        //roomName.getText();
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);
    }
}
