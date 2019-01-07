package fr.esigelec.snackio.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerConfigMenu implements Initializable {
    @FXML
    private AnchorPane configAnchorpane;

    @FXML
    private TextField roomName;

    @FXML
    private Button submit;

    @FXML
    private ChoiceBox<String> map;

    @FXML
    private ChoiceBox<String> mode;

    private Stage stage;
    private String quest;
    private Scene scene;

    public ServerConfigMenu() {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        map = new ChoiceBox<String>();
        mode = new ChoiceBox<String>();
        map.setItems(FXCollections.observableArrayList("1", "2" , "3"));
        mode.setItems(FXCollections.observableArrayList("1", "2" , "3"));
        configAnchorpane.getChildren().add(map);
        configAnchorpane.getChildren().add(mode);
        submit.setOnAction(this::submitServer);
    }

    public void submitServer(ActionEvent actionEvent) {
        //roomName.getText();
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);
    }
}
