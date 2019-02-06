package fr.esigelec.snackio.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

public class Snippet implements Initializable {
    private static MenuController.Menus previousLocation;
    private static String sert;
    @FXML
    public Button homeButton;

    @FXML
    public Button backButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button settingsButton;

    private Stage stage;
    private AudioClip audio;

    static void setPreviousLocation(MenuController.Menus previous) {
        previousLocation = previous;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        audio= new AudioClip(getClass().getResource("/sound/buttonMenuPress.wav").toString());
        homeButton.setDisable(false);
        if (sert == "YES") {
            backButton.setDisable(true);
            homeButton.setDisable(true);
        } else if (sert == "NO") {
            backButton.setDisable(false);
            homeButton.setDisable(false);
        }
        backButton.setOnAction(this::backPrevious);
        homeButton.setOnAction(this::goBackHome);
        settingsButton.setOnAction(this::openSettingsView);
        exitButton.setOnAction(this::exit);
    }

    public void exit(ActionEvent actionEvent){
        MenuController.getStage().close();
    }

    private void goBackHome(ActionEvent actionEvent) {
        audio.play();
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MAIN_MENU);
    }

    static void back(String sel) {
        sert = sel;
    }

    private void backPrevious(ActionEvent actionEvent) {
        audio.play();
        MenuController.getInstance(stage).openMenu(previousLocation);
    }

    private void openSettingsView(ActionEvent actionEvent) {
        try {
            audio= new AudioClip(getClass().getResource("/sound/buttonSnippetPress.wav").toString());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menus/controllerConfig.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Controller settings");
            stage.setScene(new Scene(root1));
            settingsButton.setDisable(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            settingsButton.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}