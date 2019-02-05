package fr.esigelec.snackio.ui.customToggles;

import fr.esigelec.snackio.game.map.MapFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

public class MapPicker extends HBox {
    private static final int SQUARE_SIDE = 67;
    private ToggleGroup mapGroup = new ToggleGroup();
    private ToggleButton chosenMap;

    public MapPicker() {
        Enum[] mapList = MapFactory.MapType.values();

        HBox mapNameBox = new HBox();
        for (Enum map : mapList) {
            // Instantiate a ToggleButton for the current map
            ToggleButton currentMapToggle = new ToggleButton();
            Text mapName = new Text();
            ImageView mapImage = new ImageView("maps/" + map.toString() + ".png");
            mapImage.setFitWidth(SQUARE_SIDE);
            mapImage.setFitHeight(SQUARE_SIDE);
            currentMapToggle.setId(map.toString());
            mapName.setText(map.toString());
            currentMapToggle.setGraphic(mapImage);
            currentMapToggle.setToggleGroup(mapGroup);

            // Add button to list
            this.getChildren().add(currentMapToggle);
            mapNameBox.getChildren().add(mapName);
        }

        // Configure toggle callback
        mapGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (mapGroup.getSelectedToggle() != null) {
                if (chosenMap != null) {
                    chosenMap.setStyle("-fx-border-color: white");
                }
                chosenMap = (ToggleButton) mapGroup.getSelectedToggle();
                ((ToggleButton) mapGroup.getSelectedToggle()).setStyle("-fx-background-color: turquoise");
            }
        });
    }

    public ToggleButton getSelectedToggle() {
        return this.chosenMap;
    }
}
