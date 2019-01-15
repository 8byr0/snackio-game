package fr.esigelec.snackio.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ComboBox<String> mode;

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
        ObservableList<String> options = FXCollections.observableArrayList();
        int i = 1;
        String img1 = "http://icons.iconarchive.com/icons/vincentburton/diaguita-ceramic-bowl/128/Diaguita-Ceramic-Bowl-"
                + i + "-icon.png";
        i = 2;
        String img2 = "http://icons.iconarchive.com/icons/vincentburton/diaguita-ceramic-bowl/128/Diaguita-Ceramic-Bowl-"
                + i + "-icon.png";
        options.addAll(img1,img2);
        map = new ComboBox(options);
        mode = new ComboBox(options);
        map.setCellFactory(c -> new StatusListCell());
        //map.setButtonCell(new StatusListCell());
        mode.setCellFactory(c -> new StatusListCell());
        //mode.setButtonCell(new StatusListCell());

        grid.add(map, 1, 1);
        grid.add(mode, 1, 4);
        submit.setOnAction(this::submitServer);
    }

    public void submitServer(ActionEvent actionEvent) {
        //roomName.getText();
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);
    }
}

class StatusListCell extends ListCell<String> {
    protected void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        setGraphic(null);
        setText(null);
        if(item!=null){

            ImageView imageView = new ImageView(new Image(item));
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            setGraphic(imageView);
            setText("choix");
        }
    }

}
