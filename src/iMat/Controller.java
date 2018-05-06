package iMat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private IMatDataHandler bc = IMatDataHandler.getInstance();

    @FXML private ToggleButton allCategoryButton;
    @FXML private ToggleButton favCategoryButton;
    @FXML private ToggleButton breadCategoryButton;
    @FXML private ToggleButton dairiesCategoryButton;
    @FXML private ToggleButton meatCategoryButton;
    @FXML private Label categoryTitleLabel;
    @FXML private Label categoryAmountLabel;

    private Map<String, ProductListItem> productListItemMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (Product product : bc.getProducts()) {
            ProductListItem productListItem = new ProductListItem(product, this);
            productListItemMap.put(product.getName(), productListItem);
        }

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
                    categoryTitleLabel.setText(selected.getText());
                }
            }
        });
    }
}
