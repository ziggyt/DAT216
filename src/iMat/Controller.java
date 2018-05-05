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

    @FXML private ToggleButton button1;
    @FXML private ToggleButton button2;
    @FXML private ToggleButton button3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final ToggleGroup categoryToggleGroup = new ToggleGroup();
        button1.setToggleGroup(categoryToggleGroup);
        button2.setToggleGroup(categoryToggleGroup);
        button3.setToggleGroup(categoryToggleGroup);
        button1.setSelected(true);

        categoryToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == null) {
                    oldValue.setSelected(true);
                }
            }
        });
    }
}
