package fr.esigelec.snackio.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menus/controllerConfig.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Controller settings");
            stage.setScene(new Scene(root1));
            settingsButton.setDisable(true);
            stage.showAndWait();
            settingsButton.setDisable(false);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}