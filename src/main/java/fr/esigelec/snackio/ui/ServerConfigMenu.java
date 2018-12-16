package fr.esigelec.snackio.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerConfigMenu implements Initializable {
    @FXML private TextField roomName;

    @FXML private ChoiceBox map;

    @FXML private ChoiceBox Mode;
    public ServerConfigMenu() {
        
    }
    @Override public void initialize(URL location, ResourceBundle resources) {

    }
}
