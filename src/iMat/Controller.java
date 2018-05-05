package iMat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private ToggleButton allCategoryButton;
    @FXML private ToggleButton favCategoryButton;
    @FXML private ToggleButton breadCategoryButton;
    @FXML private ToggleButton dairiesCategoryButton;
    @FXML private ToggleButton meatCategoryButton;
    @FXML private Label categoryTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final ToggleGroup categoryToggleGroup = new ToggleGroup();
        allCategoryButton.setToggleGroup(categoryToggleGroup);
        favCategoryButton.setToggleGroup(categoryToggleGroup);

        breadCategoryButton.setToggleGroup(categoryToggleGroup);
        dairiesCategoryButton.setToggleGroup(categoryToggleGroup);
        meatCategoryButton.setToggleGroup(categoryToggleGroup);

        allCategoryButton.setSelected(true);

        categoryToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == null) {
                    oldValue.setSelected(true);
                } else {
                    ToggleButton selected = (ToggleButton)categoryToggleGroup.getSelectedToggle();
                    categoryTitle.setText(selected.getText());
                }
            }
        });
    }
}
