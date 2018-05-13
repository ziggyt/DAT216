package iMat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.*;
import java.util.stream.Collectors;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    private IMatDataHandler bc = IMatDataHandler.getInstance();

    @FXML
    private FlowPane listFlowPane;
    @FXML
    private FlowPane cartListFlowPane;
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
    private Label cartTotalLabel;
    @FXML
    private ScrollPane productScrollPane;
    @FXML
    private AnchorPane checkoutView2;
    @FXML
    private TextField searchField;

    //Should we use enum instead? Like sortedDir BACKWARDS, FORWARDS
    private boolean sortedDirectionName = false;
    private boolean sortedDirectionPrice = false;

    private Map<String, ProductListItem> productListItemMap = new HashMap<>(); // Map of all the ProductListItems with their names as keys
    private List<Product> shownProducts; // List of products to be shown in the main view
    final private ToggleGroup categoryToggleGroup = new ToggleGroup(); // ToggleGroup for the categories in the sidebar
    private List<CartListItem> shownCartList = new ArrayList<>(); // List of CartListItems currently shown in the cart sidebar
    private ShoppingCart cart;
    private List<ShoppingItem> oldCartList = new ArrayList<>(); // Helper list made to remember which items where in the cart before the latest change
    String category = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (Product product : bc.getProducts()) {
            ProductListItem productListItem = new ProductListItem(product, this);
            productListItemMap.put(product.getName(), productListItem);
        }
        shownProducts = bc.getProducts();
        cart = bc.getShoppingCart();
        updateProductList();
        updateAmountFound();
        updateFavImage();
        updateEcoImage();
        updateCartTotal();

        listFlowPane.setHgap(42);
        listFlowPane.setVgap(21);
        listFlowPane.setPadding(new Insets(10, 10, 10,64));

        /* All available categories
    POD,
    BREAD,-------
    BERRY,
    CITRUS_FRUIT,
    HOT_DRINKS,
    COLD_DRINKS,
    EXOTIC_FRUIT,
    FISH,
    VEGETABLE_FRUIT,------
    CABBAGE,
    MEAT,----------
    DAIRIES,-------
    MELONS,
    FLOUR_SUGAR_SALT,
    NUTS_AND_SEEDS,
    PASTA,
    POTATO_RICE,
    ROOT_VEGETABLE,
    FRUIT,-----
    SWEET,
    HERB;
    */

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
                    category = selected.getId();
                    updateCurrentCategory();
                    productScrollPane.setVvalue(0); //Moves scrollposition back to top
                    updateProductList();
                    updateAmountFound();
                }
            }
        });

        cart.addShoppingCartListener(new ShoppingCartListener() {
            @Override
            public void shoppingCartChanged(CartEvent cartEvent) {
                updateCartList();
                updateCartTotal();
            }
        });

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateCurrentCategory();
                shownProducts = searchProducts(newValue);

                if (newValue.equals("")){
                    updateCurrentCategory();
                }

                updateProductList();
                updateAmountFound();

            }
        });
    }


    private void updateCurrentCategory(){
        switch (category) {
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




    }

    private List <Product> searchProducts(String text) {
        //We can't modify list directly, because we will get OOB errors
        List <Product> result = new ArrayList<>();
        for (Product shownProduct : shownProducts) {
            if (shownProduct.getName().toLowerCase().contains(text.toLowerCase())) {
                result.add(shownProduct);
            }
        }
        return result;
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

    /**
     * Adds/removes products in the cart side panel to match the backend cart
     */
    private void updateCartList(){
        // Remove items
        for (ShoppingItem si : oldCartList) { // Iterate the pre-change cart
            if(!cart.getItems().contains(si)) { // If the backend cart does not contain the item
                shownCartList.remove(oldCartList.indexOf(si)); // Remove the item
            }
        }
        // Add items
        for (ShoppingItem si : cart.getItems()) { // Iterate the backend cart
            if (!oldCartList.contains(si)) { // If the item did not exist before
                shownCartList.add(new CartListItem(si, this)); // Create and add the item
            }
        }
        // Put the items in the pane
        cartListFlowPane.getChildren().clear();
        cartListFlowPane.getChildren().addAll(shownCartList);
        // Update the helper list to prepare for the next change
        oldCartList.clear();
        oldCartList.addAll(cart.getItems());
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
    void updateFavImage() {
        for (Product product : bc.getProducts()) {
            if (bc.isFavorite(product)) {
                productListItemMap.get(product.getName()).setFavoriteItemImage(new Image(getClass().getClassLoader().getResourceAsStream("iMat/resources/favorite_item_selected.png")));
            } else {
                productListItemMap.get(product.getName()).setFavoriteItemImage(new Image(getClass().getClassLoader().getResourceAsStream("iMat/resources/favorite_item_notselected.png")));

            }
        }
    }

    public void updateEcoImage() {
        for (Product product : bc.getProducts()) {
            if (product.isEcological()) {
                productListItemMap.get(product.getName()).setEcoImage((new Image(getClass().getClassLoader().getResourceAsStream("iMat/resources/eco.png"))));
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
    private void sortByEco(){
            shownProducts.sort(Comparator.comparing(Product::isEcological).reversed());
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

    @FXML
    private void toCheckOut(){
        checkoutView2.toFront();
    }

    Image getSquareImage(Image image){

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

    Image getProductImage(Product p){

        return  bc.getFXImage(p);
    }

    boolean getFavStatus(Product p){

        return bc.isFavorite(p);
    }

    void addToFavorites(Product p){
        bc.addFavorite(p);
        updateProductList();
    }

    void removeFromFavorites(Product p){
        bc.removeFavorite(p);
        updateProductList();
    }

    void purchaseItem(Product p, int n){
        if (isInCart(p)) { // If the product is already in the cart
            for (ShoppingItem item : cart.getItems()) {
                if (item.getProduct().equals(p)) { // Search for the corresponding ShoppingItem
                    item.setAmount(item.getAmount()+n); // Add to the existing amount
                    shownCartList.get(cart.getItems().indexOf(item)).updateQuantityTextField(); // Update the quantityTextField in the CartListItem
                    break;
                }
            }
        } else {
            cart.addItem(new ShoppingItem(p, n));
        }
    }

    void removeItemFromCart(ShoppingItem si) {
        cart.removeItem(si);
    }

    void updateCartTotal() {
        cartTotalLabel.setText("Totalkostnad : " + Math.round(cart.getTotal()));

    }


    private Boolean isInCart(Product p) {
        for (ShoppingItem si : cart.getItems()) {
            if (si.getProduct().equals(p)) {
                return true;
            }
        }
        return false;
    }

}

