package iMat;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
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

    //Category buttons
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
    private ToggleButton fishCategoryButton;
    @FXML
    private ToggleButton pastaCategoryButton;
    @FXML
    private ToggleButton potatoCategoryButton;
    @FXML
    private ToggleButton rootCategoryButton;
    @FXML
    private ToggleButton flourCategoryButton;
    @FXML
    private ToggleButton herbCategoryButton;
    @FXML
    private ToggleButton fruitCategoryButton;
    @FXML
    private ToggleButton vegetablesCategoryButton;
    @FXML
    private ToggleButton berryCategoryButton;
    @FXML
    private ToggleButton nutsCategoryButton;
    @FXML
    private ToggleButton sweetsCategoryButton;
    @FXML
    private ToggleButton coldDrinksCategoryButton;
    @FXML
    private ToggleButton hotDrinksCategoryButton;


    @FXML
    private Label categoryTitleLabel;
    @FXML
    private Label categoryAmountLabel;
    @FXML
    private Label cartTotalLabel;
    @FXML
    private ScrollPane productScrollPane;
    @FXML
    private AnchorPane checkoutView1;
    @FXML
    private AnchorPane checkoutView2;
    @FXML
    private AnchorPane checkoutView3;
    @FXML
    private TextField searchField;

    //Checkoutfields for address
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField countyField;
    @FXML
    private TextField phoneField;

    //Checkoutfields for credit card
    @FXML
    private TextField creditCardField;
    @FXML
    private TextField expiryMonthField;
    @FXML
    private TextField expiryYearField;
    @FXML
    private TextField cvcField;

    //Checkout 1
    @FXML
    private FlowPane checkoutFlowPane;
    @FXML
    private Label checkoutTotalLabel;


    //Main view
    @FXML
    private BorderPane mainView;

    //DetailView
    private int detailViewAmount = 1;
    private Product detailViewProduct;
    @FXML private AnchorPane detailViewPane;
    @FXML private ImageView productImageView;
    @FXML private ImageView exitIcon;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private TextField quantityTextField;
    @FXML private Button plusButton;
    @FXML private Button minusButton;
    @FXML private Button buyButton;


    private boolean sortedDirectionName = false;
    private boolean sortedDirectionPrice = false;

    private Map<String, ProductListItem> productListItemMap = new HashMap<>(); // Map of all the ProductListItems with their names as keys
    private List<Product> shownProducts; // List of products to be shown in the main view
    final private ToggleGroup categoryToggleGroup = new ToggleGroup(); // ToggleGroup for the categories in the sidebar
    private List<CartListItem> shownCartList = new ArrayList<>(); // List of CartListItems currently shown in the cart sidebar
    private ShoppingCart cart;
    private List<ShoppingItem> oldCartList = new ArrayList<>(); // Helper list made to remember which items where in the cart before the latest change
    private String category = "";

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
        updateCartList();
        updateCartTotal();

        listFlowPane.setHgap(42);
        listFlowPane.setVgap(21);
        listFlowPane.setPadding(new Insets(10, 10, 10, 64));

        /* All available categories
    POD,
    BREAD,-------
    BERRY,-----
    CITRUS_FRUIT,
    HOT_DRINKS,-----
    COLD_DRINKS,-----
    EXOTIC_FRUIT,
    FISH,-----
    VEGETABLE_FRUIT,------
    CABBAGE,
    MEAT,----------
    DAIRIES,-------
    MELONS,
    FLOUR_SUGAR_SALT,-----
    NUTS_AND_SEEDS,
    PASTA,-----
    POTATO_RICE,-----
    ROOT_VEGETABLE,
    FRUIT,-----
    SWEET,-----
    HERB;-----
    */

        allCategoryButton.setToggleGroup(categoryToggleGroup);
        favCategoryButton.setToggleGroup(categoryToggleGroup);
        breadCategoryButton.setToggleGroup(categoryToggleGroup);
        dairiesCategoryButton.setToggleGroup(categoryToggleGroup);
        meatCategoryButton.setToggleGroup(categoryToggleGroup);
        fishCategoryButton.setToggleGroup(categoryToggleGroup);
        pastaCategoryButton.setToggleGroup(categoryToggleGroup);
        potatoCategoryButton.setToggleGroup(categoryToggleGroup);
        rootCategoryButton.setToggleGroup(categoryToggleGroup);
        flourCategoryButton.setToggleGroup(categoryToggleGroup);
        herbCategoryButton.setToggleGroup(categoryToggleGroup);
        fruitCategoryButton.setToggleGroup(categoryToggleGroup);
        vegetablesCategoryButton.setToggleGroup(categoryToggleGroup);
        berryCategoryButton.setToggleGroup(categoryToggleGroup);
        nutsCategoryButton.setToggleGroup(categoryToggleGroup);
        sweetsCategoryButton.setToggleGroup(categoryToggleGroup);
        coldDrinksCategoryButton.setToggleGroup(categoryToggleGroup);
        hotDrinksCategoryButton.setToggleGroup(categoryToggleGroup);
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

                if (newValue.equals("")) {
                    updateCurrentCategory();
                }

                updateProductList();
                updateAmountFound();

            }
        });

        postalCodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    // If text is set to longer than 5
                    if (postalCodeField.getText().length() >= 5) {
                        // Set length to 5
                        postalCodeField.setText(postalCodeField.getText().substring(0, 5));
                    }
                    // Check for non numeric characters
                    if (!newValue.matches("\\d*")) {
                        // Remove all non numeric characters
                        newValue = newValue.replaceAll("[^\\d]", "");
                        postalCodeField.setText(newValue);
                        System.out.println("Only numbers are allowed"); //Just for now, we can make a prompt popup on screen
                    }
                }
            }
        });
        phoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    // Check for non numeric characters
                    if (!newValue.matches("\\d*")) {
                        // Remove all non numeric characters
                        newValue = newValue.replaceAll("[^\\d]", "");
                        phoneField.setText(newValue);
                        System.out.println("Only numbers are allowed"); //Just for now, we can make a prompt popup on screen
                    }
                }
            }
        });
        creditCardField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    // If text is set to longer than 16
                    if (creditCardField.getText().length() >= 16) {
                        // Set length to 16
                        creditCardField.setText(creditCardField.getText().substring(0, 16));
                    }
                    // Check for non numeric characters
                    if (!newValue.matches("\\d*")) {
                        // Remove all non numeric characters
                        newValue = newValue.replaceAll("[^\\d]", "");
                        creditCardField.setText(newValue);
                        System.out.println("Only numbers are allowed"); //Just for now, we can make a prompt popup on screen
                    }
                }
            }
        });
        expiryMonthField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    // If text is set to longer than 2
                    if (expiryMonthField.getText().length() >= 2) {
                        // Set length to 2
                        expiryMonthField.setText(expiryMonthField.getText().substring(0, 2));
                    }
                    // Check for non numeric characters
                    if (!newValue.matches("\\d*")) {
                        // Remove all non numeric characters
                        newValue = newValue.replaceAll("[^\\d]", "");
                        expiryMonthField.setText(newValue);
                        System.out.println("Only numbers are allowed"); //Just for now, we can make a prompt popup on screen
                    }
                }
            }
        });
        expiryYearField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    // If text is set to longer than 2
                    if (expiryYearField.getText().length() >= 2) {
                        // Set length to 2
                        expiryYearField.setText(expiryYearField.getText().substring(0, 2));
                    }
                    // Check for non numeric characters
                    if (!newValue.matches("\\d*")) {
                        // Remove all non numeric characters
                        newValue = newValue.replaceAll("[^\\d]", "");
                        expiryYearField.setText(newValue);
                        System.out.println("Only numbers are allowed"); //Just for now, we can make a prompt popup on screen
                    }
                }
            }
        });
        cvcField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    // If text is set to longer than 3
                    if (cvcField.getText().length() >= 3) {
                        // Set length to 3
                        cvcField.setText(cvcField.getText().substring(0, 3));
                    }
                    // Check for non numeric characters
                    if (!newValue.matches("\\d*")) {
                        // Remove all non numeric characters
                        newValue = newValue.replaceAll("[^\\d]", "");
                        cvcField.setText(newValue);
                        System.out.println("Only numbers are allowed"); //Just for now, we can make a prompt popup on screen
                    }
                }
            }
        });
    }


    private void updateCurrentCategory() {
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
            case "fishCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.FISH);
                break;
            case "pastaCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.PASTA);
                break;
            case "potatoCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.POTATO_RICE);
                break;
            case "rootCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.ROOT_VEGETABLE);
                break;
            case "flourCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.FLOUR_SUGAR_SALT);
                break;
            case "herbCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.HERB);
                break;
            case "fruitCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.FRUIT);
                break;
            case "vegetablesCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.VEGETABLE_FRUIT);
                break;
            case "berryCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.BERRY);
                break;
            case "nutsCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.NUTS_AND_SEEDS);
                break;
            case "sweetsCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.SWEET);
                break;
            case "coldDrinksCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.COLD_DRINKS);
                break;
            case "hotDrinksCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.HOT_DRINKS);
                break;
        }


    }

    private List<Product> searchProducts(String text) {
        //We can't modify list directly, because we will get OOB errors
        List<Product> result = new ArrayList<>();
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
    private void updateCartList() {
        // Remove items
        for (int i = oldCartList.size()-1; i >= 0; i--) { // Iterate the pre-change cart, has to be in reverse order (when removing multiple items at once) because when removing an item from shownCartList the indexes of all the items after it changes
            ShoppingItem si = oldCartList.get(i);
            if (!cart.getItems().contains(si)) { // If the backend cart does not contain the item
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


    private void updateCheckoutCart(){
        // Put the items in the pane
        checkoutFlowPane.getChildren().clear();
        checkoutFlowPane.getChildren().addAll(shownCartList);
        // Update the helper list to prepare for the next change
        oldCartList.clear();
        oldCartList.addAll(cart.getItems());

        updateCheckoutTotal();


    }

    private void populateDetailView( Product product ){
        productImageView.setImage(getSquareImage(getProductImage(product)));
        nameLabel.setText(product.getName());
        priceLabel.setText(product.getPrice() + " " + product.getUnit());
        detailViewProduct = product;
    }

    @FXML
    private void incAmount(){
        if(detailViewAmount<99) {
            detailViewAmount++;
        }
        quantityTextField.setText(detailViewAmount + " st");
    }

    @FXML
    private void decAmount(){
        if(detailViewAmount>1) {
            detailViewAmount--;
        }
        quantityTextField.setText(detailViewAmount + " st");
    }

    @FXML
    private void detailViewPurchase(){
        purchaseItem(detailViewProduct, detailViewAmount);
        updateCartTotal();
        detailViewAmount=1;
        quantityTextField.setText(detailViewAmount + " st");
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

    private void updateEcoImage() {
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
    private void sortByEco() {
        shownProducts.sort(Comparator.comparing(Product::isEcological).reversed());
        updateProductList();
    }


    @FXML
    private void sortByPrice() {
        if (!sortedDirectionPrice) {
            shownProducts.sort(Comparator.comparing(Product::getPrice));
        } else {
            Collections.reverse(shownProducts);
        }
        sortedDirectionPrice = !sortedDirectionPrice;
        updateProductList();

    }

    @FXML
    public void openDetailView( Product product ){
        populateDetailView(product);
        detailViewPane.toFront();
    }

    // Separate "finish" and "back to" methods because it matters if you are completing a step or just going back to a previous one
    @FXML
    private void toCheckout() {
        checkoutView1.toFront();
        updateCheckoutCart();
    }

    @FXML
    private void finishCheckoutStep1() {
        checkoutView2.toFront();
        autoFill(); // fill the text fields in step 2 automatically
    }

    @FXML
    private void finishCheckoutStep2() {
        if(checkAllAddressFields()) {
            bc.getCustomer().setFirstName(firstNameField.getText());
            bc.getCustomer().setLastName(lastNameField.getText());
            bc.getCustomer().setAddress(addressField.getText());
            bc.getCustomer().setPostCode(postalCodeField.getText());
            bc.getCustomer().setPostAddress(countyField.getText());
            bc.getCustomer().setPhoneNumber(phoneField.getText());

            if (bc.isCustomerComplete()) {
                checkoutView3.toFront();
            }
        }
    }

    @FXML
    private void finishCheckout() {
        if(checkAllCreditCardFields()) {
            bc.getCreditCard().setCardNumber(creditCardField.getText());
            bc.getCreditCard().setValidMonth(Integer.parseInt(expiryMonthField.getText()));
            bc.getCreditCard().setValidYear(Integer.parseInt(expiryYearField.getText()));
            bc.getCreditCard().setVerificationCode(Integer.parseInt(cvcField.getText()));

            bc.placeOrder(); // Saves the order placement and clears the shopping cart
            mainView.toFront();
        }
    }

    @FXML
    private void backToCheckoutStep1() {
        checkoutView1.toFront();
    }

    @FXML
    private void backToCheckoutStep2() {
        checkoutView2.toFront();
    }

    @FXML
    private void goHome() {
        mainView.toFront();
        updateCartList();
    }

    Image getSquareImage(Image image) {

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if (image.getWidth() > image.getHeight()) {
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int) (image.getWidth() - width) / 2;
            y = 0;
        } else if (image.getHeight() > image.getWidth()) {
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height) / 2;
        } else {
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }


    Image getProductImage(Product p) {

        return bc.getFXImage(p);
    }

    boolean getFavStatus(Product p) {

        return bc.isFavorite(p);
    }

    void addToFavorites(Product p) {
        bc.addFavorite(p);
        updateProductList();
    }

    void removeFromFavorites(Product p) {
        bc.removeFavorite(p);
        updateProductList();
    }

    void purchaseItem(Product p, int n) {
        if (isInCart(p)) { // If the product is already in the cart
            for (ShoppingItem item : cart.getItems()) {
                if (item.getProduct().equals(p)) { // Search for the corresponding ShoppingItem
                    item.setAmount(item.getAmount() + n); // Add to the existing amount
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
        cartTotalLabel.setText("Totalkostnad : " + Math.round(cart.getTotal())+ "kr");

    }

    void updateCheckoutTotal() {
        checkoutTotalLabel.setText("Totalkostnad : " + Math.round(cart.getTotal())+ "kr");

    }


    private Boolean isInCart(Product p) {
        for (ShoppingItem si : cart.getItems()) {
            if (si.getProduct().equals(p)) {
                return true;
            }
        }
        return false;
    }


    /*

    Checkout functionality below in order to preserve some sort of order...

     */


    private void autoFill() {
        firstNameField.setText(bc.getCustomer().getFirstName());
        lastNameField.setText(bc.getCustomer().getLastName());
        addressField.setText(bc.getCustomer().getAddress());
        postalCodeField.setText(bc.getCustomer().getPostCode());
        countyField.setText(bc.getCustomer().getPostAddress());
        phoneField.setText(bc.getCustomer().getPhoneNumber());
    }

    @FXML
    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        postalCodeField.setText("");
        countyField.setText("");
        phoneField.setText("");

    }

    //I guess you could make an array/list of fields but this will be ok
    private boolean checkAllAddressFields() {
        return(
        checkField(firstNameField) &
        checkField(lastNameField) &
        checkField(addressField) &
        checkField(postalCodeField) &
        checkField(countyField) &
        checkField(phoneField)
        );
    }

    private boolean checkAllCreditCardFields(){
        return(
        checkField(creditCardField) &
        checkField(expiryMonthField) &
        checkField(expiryYearField) &
        checkField(cvcField)
        );
    }

    private boolean checkField(TextField t) {
        if (t.getText().equals("")) {
            missingTextAlert(t);
            return false;
        }
        return true;
    }


    private void missingTextAlert(TextField t) {
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), t);
        fade.setFromValue(1); //the pane is invisible to start
        fade.setToValue(0); //fades in to almost completely solid
        fade.setCycleCount(4); //the amount of times the animation plays (fade in + out = 2)
        fade.setAutoReverse(true); //in order to make it go from solid to transparent
        fade.play(); // start animation

    }

    @FXML
    public void closeButtonMouseEntered(){
        exitIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed(){
        exitIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited(){
        exitIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close.png")));
    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
    }

}

