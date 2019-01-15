package fr.esigelec.snackio.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
public class Snippet implements Initializable {
    public static MenuController.Menus previousLocation;
    public static String sert;
    @FXML
    public Button homeButton;

    @FXML
    public Button backButton;

    @FXML
    private Button settingsButton;

    private Stage stage;

    public Snippet() {


    }

    public static void setPreviousLocation(MenuController.Menus previous) {
        previousLocation = previous;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeButton.setDisable(false);
        if(sert=="YES"){
            backButton.setDisable(true);
            homeButton.setDisable(true);
        }else if (sert=="NO"){
            backButton.setDisable(false);
            homeButton.setDisable(false);
        }
        backButton.setOnAction(this::backPrevious);
        homeButton.setOnAction(this::backtoHome);
        settingsButton.setOnAction(this::opensettingsView);

    }

    public void backtoHome(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MAIN_MENU);
    }

    public static void back(String sel){
        sert=sel;
    }

    public void backPrevious(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(previousLocation);
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