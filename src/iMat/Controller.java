package iMat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.*;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    private IMatDataHandler bc = IMatDataHandler.getInstance();

    @FXML
    private FlowPane listFlowPane;
    @FXML
    private FlowPane registerFlowPane;
    @FXML
    private ToggleButton allCategoryButton;
    @FXML
    private ToggleButton favCategoryButton;
    @FXML
    private ToggleButton breadCategoryButton;
    @FXML
    private ToggleButton dairiesCategoryButton;
    @FXML
    private ToggleButton meatCategoryButton;
    @FXML
    private ToggleButton fruitCategoryButton;
    @FXML
    private ToggleButton vegetablesCategoryButton;
    @FXML
    private Label categoryTitleLabel;
    @FXML
    private Label categoryAmountLabel;
    @FXML
    private Button sortByNameButton;
    @FXML
    private Button sortByPriceButton;

    //Should we use enum instead? Like sortedDir BACKWARDS, FORWARDS
    private boolean sortedDirectionName = false;
    private boolean sortedDirectionPrice = false;

    private Map<String, ProductListItem> productListItemMap = new HashMap<>(); // Map of all the Products with their names as keys
    private List<Product> shownProducts; // List of products to be shown in the main view
    private ShoppingCart shoppingCart;
    final private ToggleGroup categoryToggleGroup = new ToggleGroup(); // ToggleGroup for the categories in the sidebar

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (Product product : bc.getProducts()) {
            ProductListItem productListItem = new ProductListItem(product, this);
            productListItemMap.put(product.getName(), productListItem);
        }
        shownProducts = bc.getProducts();
        updateProductList();
        updateAmountFound();
        updateFavImage();

        listFlowPane.setHgap(21);
        listFlowPane.setVgap(21);

        allCategoryButton.setToggleGroup(categoryToggleGroup);
        favCategoryButton.setToggleGroup(categoryToggleGroup);
        breadCategoryButton.setToggleGroup(categoryToggleGroup);
        dairiesCategoryButton.setToggleGroup(categoryToggleGroup);
        meatCategoryButton.setToggleGroup(categoryToggleGroup);
        fruitCategoryButton.setToggleGroup(categoryToggleGroup);
        vegetablesCategoryButton.setToggleGroup(categoryToggleGroup);
        allCategoryButton.setSelected(true);

        categoryToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == null) {
                    oldValue.setSelected(true); // Stops the currently selected toggle from being unselected when pressed
                } else {

                    ToggleButton selected = (ToggleButton) categoryToggleGroup.getSelectedToggle();
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
                        case "fruitCategoryButton":
                            shownProducts = bc.getProducts(ProductCategory.FRUIT);
                            break;
                        case "vegetablesCategoryButton":
                            shownProducts = bc.getProducts(ProductCategory.VEGETABLE_FRUIT);
                            break;
                    }
                    updateProductList();
                    updateAmountFound();
                }
            }
        });
    }

    /**
     * Adds the products from the shownProducts list to the listFlowPane in the main view
     */
    private void updateProductList() {
        listFlowPane.getChildren().clear();

        for (Product product : shownProducts) {
            listFlowPane.getChildren().add(productListItemMap.get(product.getName()));
        }
    }

    public void updateRegisterItemList(ShoppingItem item) {

    }

    private void updateShoppingcart(){
        shoppingCart = bc.getShoppingCart();
    }

    /**
     * Shows the amount of shown products
     */
    private void updateAmountFound() {
        categoryAmountLabel.setText("(" + shownProducts.size() + " träffar)");

    }

    /**
     * Iterates through the ProductListItems using the name property of a product in order to set correct image (filled star vs empty star)
     */

    public void updateFavImage() {
        for (Product product : bc.getProducts()) {
            if (bc.isFavorite(product)) {
                productListItemMap.get(product.getName()).setFavoriteItemImage(new Image(getClass().getClassLoader().getResourceAsStream("iMat/resources/favorite_item_selected.png")));
            } else {
                productListItemMap.get(product.getName()).setFavoriteItemImage(new Image(getClass().getClassLoader().getResourceAsStream("iMat/resources/favorite_item_notselected.png")));

            }
        }
    }

    @FXML
    private void sortByName() {
        if (!sortedDirectionName) {
            shownProducts.sort(Comparator.comparing(Product::getName));
        } else {
            Collections.reverse(shownProducts);
        }
        sortedDirectionName = !sortedDirectionName;
        updateProductList();
    }



    @FXML
    private void sortByPrice(){
        if(!sortedDirectionPrice) {
            shownProducts.sort(Comparator.comparing(Product::getPrice));
        }
        else {
            Collections.reverse(shownProducts);
        }
        sortedDirectionPrice = !sortedDirectionPrice;
        updateProductList();

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

    //Helpers, here for now. Accessed from ProductListItem

    public Image getProductImage(Product p){

        return  bc.getFXImage(p);
    }

    public boolean getFavStatus(Product p){

        return bc.isFavorite(p);
    }

    public void addToFavorites(Product p){
        bc.addFavorite(p);
        updateProductList();
    }

    public void removeFromFavorites(Product p){
        bc.removeFavorite(p);
        updateProductList();
    }

    public void purchaseItem(Product p, int amount){
        bc.getShoppingCart().addItem(new ShoppingItem(p, amount));
        updateShoppingcart();


    }
}

