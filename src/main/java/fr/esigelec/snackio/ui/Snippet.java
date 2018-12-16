package fr.esigelec.snackio.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
public class Snippet implements Initializable {
    @FXML
    public Button homeButton;

    @FXML
    public Button backButton;

    @FXML
    private Button settingsButton;

    private Stage stage;

    public Snippet() {


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
<<<<<<< HEAD
<<<<<<< HEAD
        backButton.setDisable(true);
=======
        //homeButton.setDisable(true);
        backButton.setOnAction(this::backPrevious);
>>>>>>> ServerConfigMenu
=======
        homeButton.setDisable(true);
        //backButton.setOnAction(this::backPrevious);
>>>>>>> ServerConfigMenu
        homeButton.setOnAction(this::backtoHome);
        settingsButton.setOnAction(this::opensettingsView);

    }
    public void backtoHome(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MAIN_MENU);
    }
    public void backPrevious(ActionEvent actionEvent) {
        System.out.println("previous page");
    }

    public void opensettingsView(ActionEvent actionEvent){
        System.out.println("settings");
    }
}