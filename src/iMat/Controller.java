package iMat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private IMatDataHandler bc = IMatDataHandler.getInstance();

    @FXML private FlowPane listFlowPane;
    @FXML private ToggleButton allCategoryButton;
    @FXML private ToggleButton favCategoryButton;
    @FXML private ToggleButton breadCategoryButton;
    @FXML private ToggleButton dairiesCategoryButton;
    @FXML private ToggleButton meatCategoryButton;
    @FXML private Label categoryTitleLabel;
    @FXML private Label categoryAmountLabel;

    private Map<String, ProductListItem> productListItemMap = new HashMap<>(); // Map of all the Products with their names as keys
    private List<Product> shownProducts; // List of products to be shown in the main view
    final private ToggleGroup categoryToggleGroup = new ToggleGroup(); // ToggleGroup for the categories in the sidebar

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (Product product : bc.getProducts()) {
            ProductListItem productListItem = new ProductListItem(product, this);
            productListItemMap.put(product.getName(), productListItem);
        }
        shownProducts = bc.getProducts();
        updateProductList();

        listFlowPane.setHgap(21);
        listFlowPane.setVgap(21);

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
                    oldValue.setSelected(true); // Stops the currently selected toggle from being unselected when pressed
                } else {

                    ToggleButton selected = (ToggleButton)categoryToggleGroup.getSelectedToggle();
                    categoryTitleLabel.setText(selected.getText());

                    switch (selected.getId()) {
                        case "allCategoryButton":
                            shownProducts = bc.getProducts();
                            break;
                        case "favCategoryButton":
                            shownProducts = bc.favorites();
                            break;
                        case "breadCategoryButton":
                            shownProducts = bc.getProducts(ProductCategory.BREAD);
                            break;
                        case "dairiesCategoryButton":
                            shownProducts = bc.getProducts(ProductCategory.DAIRIES);
                            break;
                        case "meatCategoryButton":
                            shownProducts = bc.getProducts(ProductCategory.MEAT);
                            break;
                    }
                    updateProductList();
                }
            }
        });
    }

    /**
     * Adds the products from the shownProducts list to the listFlowPane in th main view
     */
    private void updateProductList(){
        listFlowPane.getChildren().clear();

        for (Product product : shownProducts) {
            listFlowPane.getChildren().add(productListItemMap.get(product.getName()));
        }
    }

    public Image getSquareImage(Image image){

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if(image.getWidth() > image.getHeight()){
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }

        else if(image.getHeight() > image.getWidth()){
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }

        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }
}
