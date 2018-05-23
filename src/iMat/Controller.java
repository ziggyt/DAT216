package iMat;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.*;

import javax.tools.Tool;
import java.net.URL;
import java.time.LocalDate;
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
    private ToggleButton podCategoryButton;
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
    private Label cartAmountLabel;
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
    private ScrollPane checkoutCartScrollPane;
    @FXML
    private Label checkoutTotalLabel;

    //Checkout 2
    @FXML
    private Label checkoutErrorLabel;
    @FXML
    private Label deliveryTimeErrorLabel;
    @FXML
    private DatePicker checkoutDatePicker;
    @FXML
    private AnchorPane deliveryTimePane;
    @FXML
    private RadioButton deliveryTimeRadioButton1;
    @FXML
    private RadioButton deliveryTimeRadioButton2;
    @FXML
    private RadioButton deliveryTimeRadioButton3;
    @FXML
    private StackPane deliveryTimeTooltipPane;

    //Checkout 3
    @FXML
    private Button placeOrderButton;
    @FXML
    private StackPane cvcTooltipPane;
    @FXML
    private Label checkoutErrorLabel2;

    //Main view
    @FXML
    private BorderPane mainView;
    @FXML
    private ImageView blurSearchBar;
    @FXML
    private ImageView trashCanImageView;
    @FXML
    private ScrollPane cartScrollPane;
    @FXML
    private ImageView logoImageView;

    //PurchaseHistory View
    @FXML
    private AnchorPane purchaseHistoryPane;
    @FXML
    private FlowPane purchaseHistoryFlowPane;
    @FXML
    private FlowPane purchaseHistoryProductFlowPane;
    @FXML
    private Button purchaseOrderButton;
    @FXML
    private Button replaceShoppingCartButton;

    private Order selectedOrder;

    //Help View
    @FXML
    private AnchorPane helpPane;

    //DetailView
    private int detailViewAmount = 1;
    private Product detailViewProduct;
    @FXML
    private AnchorPane detailViewPane;
    @FXML
    private ImageView productImageView;
    @FXML
    private ImageView exitIcon;
    @FXML
    private Label nameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private TextField quantityTextField;
    @FXML
    private ImageView ecoImageView;
    @FXML
    private Button plusButton;
    @FXML
    private Button minusButton;
    @FXML
    private Button buyButton;

    //Messageprompt
    @FXML
    private AnchorPane messagePane;
    @FXML
    private TextArea messageField;
    @FXML
    private Label messageName;
    @FXML
    private ImageView messageExitIcon;


    //Sorting arrows
    @FXML
    private ImageView nameUp;
    @FXML
    private ImageView nameDown;
    @FXML
    private ImageView priceUp;
    @FXML
    private ImageView priceDown;

    //Images
    private final static Image favorite_item_selected = new Image("iMat/resources/favorite_item_selected.png");
    private final static Image favorite_item_notselected = new Image("iMat/resources/favorite_item_notselected.png");

    //Messages
    private Message emptyCartMessage = new Message("Tom kundvagn", "Din kundvagn är tom, testa att lägga till lite varor");
    private Message missingFieldText = new Message("Information saknas i textfält", "Alla fälten måste vara ifyllda för att kunna gå vidare");
    private Message missingDeliveryTimeText = new Message("Information saknas i leveranstidväljaren", "Du måste välja en leveranstid för att kunna gå vidare");

    //private Message invalidMonthMessage = new Message("Ogiltig månad", "")
    private boolean sortedDirectionName = false;
    private boolean sortedDirectionPrice = false;
    private boolean sortedDirectionEco = false;
    private boolean sortedAscending = true;

    private boolean searchFieldBlurred = false;


    private boolean inCheckout = false; // To decide which cart flow pane to fill in updateCartList method

    private Map<String, ProductListItem> productListItemMap = new HashMap<>(); // Map of all the ProductListItems with their names as keys
    private List<Product> shownProducts; // List of products to be shown in the main view

    final private ToggleGroup categoryToggleGroup = new ToggleGroup(); // ToggleGroup for the categories in the sidebar
    final private ToggleGroup purchaseHistoryToggleGroup = new ToggleGroup(); //Togglegroup for the purchaseHistoryList
    final private ToggleGroup deliveryTimeToggleGroup = new ToggleGroup(); // ToggleGroup for the radio buttons for delivery time in checkout step 2
    private List<CartListItem> shownCartList = new ArrayList<>(); // List of CartListItems currently shown in the cart sidebar
    private ShoppingCart cart;
    private List<ShoppingItem> oldCartList = new ArrayList<>(); // Helper list made to remember which items where in the cart before the latest change
    private String category = "";
    private String deliveryTime = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater( () -> allCategoryButton.requestFocus()); // Initial focused element

        for (Product product : bc.getProducts()) {
            ProductListItem productListItem = new ProductListItem(product, this);
            productListItemMap.put(product.getName(), productListItem);
        }

        //bc.reset();

        shownProducts = bc.getProducts();
        cart = bc.getShoppingCart();
        updateProductList();
        updateAmountFound();
        updateFavImage();
        updateEcoImage();
        updateCartList();
        updateCartTotal();
        sortByName();

        listFlowPane.setHgap(42);
        listFlowPane.setVgap(21);
        listFlowPane.setPadding(new Insets(10, 10, 10, 55));

        bindTooltip(trashCanImageView, new Tooltip("Ta bort all varor från varukorgen")); // Trash cart tooltip
        bindTooltip(ecoImageView, new Tooltip("Den här produkten är ekologisk")); // For the detailView only
        bindTooltip(cvcTooltipPane, new Tooltip("Den tresiffriga koden på baksidan av kortet")); // CVC tooltip
        bindTooltip(deliveryTimeTooltipPane, new Tooltip("Välj det datum och den tid du vill att dina varor ska vara hemma hos dig")); // delivery time selection tooltip

        resetCheckoutErrorLabels();

        checkoutDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now().plusDays(1)));
            }
        });

        checkoutDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (newValue != null) {
                    deliveryTimeErrorLabel.setText("");
                    checkoutDatePicker.setStyle(null);

                    if (deliveryTimePane.isDisabled()) {
                        deliveryTimePane.setDisable(false); // When date is picked, enable delivery time selection
                        deliveryTimeRadioButton2.setSelected(true); // Default selected
                    }
                }
            }
        });

        deliveryTimeRadioButton1.setToggleGroup(deliveryTimeToggleGroup);
        deliveryTimeRadioButton2.setToggleGroup(deliveryTimeToggleGroup);
        deliveryTimeRadioButton3.setToggleGroup(deliveryTimeToggleGroup);

        deliveryTimeToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                deliveryTime = ((RadioButton) deliveryTimeToggleGroup.getSelectedToggle()).getText();
            }
        });

        /* All available categories
    POD,-----
    BREAD,-------
    BERRY,-----
    CITRUS_FRUIT,------
    HOT_DRINKS,-----
    COLD_DRINKS,-----
    EXOTIC_FRUIT,------
    FISH,-----
    VEGETABLE_FRUIT,------
    CABBAGE,-----
    MEAT,----------
    DAIRIES,-------
    MELONS,-----
    FLOUR_SUGAR_SALT,-----
    NUTS_AND_SEEDS,-----
    PASTA,-----
    POTATO_RICE,-----
    ROOT_VEGETABLE,-----
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
        podCategoryButton.setToggleGroup(categoryToggleGroup);
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
        category = allCategoryButton.getId();


        categoryToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == null) {
                    oldValue.setSelected(true); // Stops the currently selected toggle from being unselected when pressed
                } else {
                    if (newValue!=allCategoryButton) {
                        searchField.clear();
                    }
                    ToggleButton selected = (ToggleButton) categoryToggleGroup.getSelectedToggle();
                    categoryTitleLabel.setText(selected.getText());
                    category = selected.getId();
                    updateCurrentCategory();
                    sort();
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
            }
        });

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    allCategoryButton.setSelected(true);
                    updateCurrentCategory();
                    shownProducts = searchProducts(newValue);
                } else {
                    updateCurrentCategory();
                }

                sort();
                productScrollPane.setVvalue(0); //Moves scrollposition back to top
                updateProductList();
                updateAmountFound();

            }
        });

        firstNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    if (addressFieldsAreFilled()) {
                        checkoutErrorLabel.setText("");
                    }
                    firstNameField.setStyle(null);
                }
            }
        });
        lastNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    if (addressFieldsAreFilled()) {
                        checkoutErrorLabel.setText("");
                    }
                    lastNameField.setStyle(null);
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

                    if (addressFieldsAreFilled()) {
                        checkoutErrorLabel.setText("");
                    }
                    phoneField.setStyle(null);
                }
            }
        });
        addressField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    if (addressFieldsAreFilled()) {
                        checkoutErrorLabel.setText("");
                    }
                    addressField.setStyle(null);
                }
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

                    if (addressFieldsAreFilled()) {
                        checkoutErrorLabel.setText("");
                    }
                    postalCodeField.setStyle(null);
                }
            }
        });
        countyField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    if (addressFieldsAreFilled()) {
                        checkoutErrorLabel.setText("");
                    }
                    countyField.setStyle(null);
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

                    if (creditCardFieldsAreFilled()) {
                        checkoutErrorLabel2.setText("");
                    }
                    creditCardField.setStyle(null);
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

                    if (creditCardFieldsAreFilled()) {
                        checkoutErrorLabel2.setText("");
                    }
                    expiryMonthField.setStyle(null);
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

                    if (creditCardFieldsAreFilled()) {
                        checkoutErrorLabel2.setText("");
                    }
                    expiryYearField.setStyle(null);
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

                    if (creditCardFieldsAreFilled()) {
                        checkoutErrorLabel2.setText("");
                    }
                    cvcField.setStyle(null);
                }
            }
        });
    }
    // Sorting dir listeners


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
            case "podCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.POD);
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
                shownProducts.addAll(bc.getProducts(ProductCategory.CITRUS_FRUIT));
                shownProducts.addAll(bc.getProducts(ProductCategory.EXOTIC_FRUIT));
                shownProducts.addAll(bc.getProducts(ProductCategory.MELONS));
                break;
            case "vegetablesCategoryButton":
                shownProducts = bc.getProducts(ProductCategory.VEGETABLE_FRUIT);
                shownProducts.addAll(bc.getProducts(ProductCategory.CABBAGE));
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
        for (int i = oldCartList.size() - 1; i >= 0; i--) { // Iterate the pre-change cart, has to be in reverse order (when removing multiple items at once) because when removing an item from shownCartList the indexes of all the items after it changes
            ShoppingItem si = oldCartList.get(i);
            if (!cart.getItems().contains(si)) { // If the backend cart does not contain the item
                shownCartList.remove(oldCartList.indexOf(si)); // Remove the item
            }
        }
        // Add items
        for (ShoppingItem si : cart.getItems()) { // Iterate the backend cart
            if (!oldCartList.contains(si)) { // If the item did not exist before
                shownCartList.add(new CartListItem(si, this)); // Create and add the item
                Platform.runLater( () -> cartScrollPane.setVvalue(cartScrollPane.getVmax())); // Scroll down in the cart so you can see the new item
            }
        }
        // Put the items in the pane
        if (inCheckout) {
            updateCheckoutCart();
        } else {
            cartListFlowPane.getChildren().clear();
            cartListFlowPane.getChildren().addAll(shownCartList);
            updateCartTotal();
        }
        // Update the helper list to prepare for the next change
        oldCartList.clear();
        oldCartList.addAll(cart.getItems());
    }


    private void updateCheckoutCart() {
        // Put the items in the pane
        checkoutFlowPane.getChildren().clear();
        checkoutFlowPane.getChildren().addAll(shownCartList);

        updateCheckoutTotal();
    }

    @FXML
    private void toPurchaseHistory() {
        searchField.clear();
        blurInSearchBar();
        populatePurchaseHistory();
        purchaseHistoryPane.toFront();
        helpPane.toBack(); // Because of the transparent background and when closing the purchase history pane

        if (bc.getOrders().isEmpty()) {
            purchaseOrderButton.setDisable(true);
            replaceShoppingCartButton.setDisable(true);
        } else {
            purchaseOrderButton.setDisable(false);
            replaceShoppingCartButton.setDisable(false);
        }
    }

    @FXML
    private void toHelp() {
        searchField.clear();
        blurInSearchBar();
        helpPane.toFront();
        purchaseHistoryPane.toBack(); // Because of the transparent background and when closing the help pane
    }

    private void populatePurchaseHistory(){
        purchaseHistoryFlowPane.getChildren().clear();

        List <Order> orders =  bc.getOrders();
        for(int i = orders.size()-1; i >= 0; i--){
            if(i==orders.size()-1){
                PurchaseHistoryListItem firstItem = new PurchaseHistoryListItem(orders.get(i), this, purchaseHistoryToggleGroup);
                purchaseHistoryFlowPane.getChildren().add(firstItem);
                firstItem.setSelected();

            } else {
                purchaseHistoryFlowPane.getChildren().add(new PurchaseHistoryListItem(orders.get(i), this, purchaseHistoryToggleGroup));
            }
        }

    }

    public void populatePurchaseHistoryProductList(Order order){

        selectedOrder = order;

        purchaseHistoryProductFlowPane.getChildren().clear();

        for(ShoppingItem item : order.getItems()){
            purchaseHistoryProductFlowPane.getChildren().add(new PurchaseHistoryProductListItem(this, item));
        }
    }

    @FXML
    private void replaceShoppingCart(){
        emptyCart();

        for(ShoppingItem item : selectedOrder.getItems()){
            purchaseItem(item.getProduct(), (int)item.getAmount());
        }
        goHome();
    }

    @FXML
    private void purchaseOrder(){
        for(ShoppingItem item: selectedOrder.getItems()){
            purchaseItem(item.getProduct(), (int)item.getAmount());
        }
        goHome();
    }

    private void populateDetailView(Product product) {
        productImageView.setImage(getSquareImage(getProductImage(product)));
        nameLabel.setText(product.getName());
        priceLabel.setText(product.getPrice() + " " + product.getUnit());
        detailViewProduct = product;
        if (product.isEcological()) {
            ecoImageView.setVisible(true);
        } else {
            ecoImageView.setVisible(false);
        }
    }

    @FXML
    private void incAmount() {
        if (detailViewAmount < 99) {
            detailViewAmount++;
        }
        quantityTextField.setText(detailViewAmount + " st");
    }

    @FXML
    private void decAmount() {
        if (detailViewAmount > 1) {
            detailViewAmount--;
        }
        quantityTextField.setText(detailViewAmount + " st");
    }

    @FXML
    private void detailViewPurchase() {
        purchaseItem(detailViewProduct, detailViewAmount);
        updateCartTotal();
        detailViewAmount = 1;
        quantityTextField.setText(detailViewAmount + " st");
    }

    /**
     * Shows the amount of shown products
     */
    private void updateAmountFound() {
        categoryAmountLabel.setText("(" + shownProducts.size() + " träffar)");

    }


    void resetArrows() {
        setArrowFaded(nameDown);
        setArrowFaded(nameUp);
        setArrowFaded(priceDown);
        setArrowFaded(priceUp);
    }

    void resetDirs() {
        sortedDirectionEco = false;
        sortedDirectionPrice = false;
        sortedDirectionName = false;

    }

    /**
     * Iterates through the ProductListItems using the name property of a product in order to set correct image (filled star vs empty star)
     */
    void updateFavImage() {
        for (Product product : bc.getProducts()) {
            if (bc.isFavorite(product)) {
                productListItemMap.get(product.getName()).setFavoriteItemImage(favorite_item_selected);
            } else {
                productListItemMap.get(product.getName()).setFavoriteItemImage(favorite_item_notselected);

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

    private void sort() {
        if (sortedDirectionName) {
            shownProducts.sort(Comparator.comparing(Product::getName));
        } else if (sortedDirectionPrice) {
            shownProducts.sort(Comparator.comparing(Product::getPrice));
        }

        if (sortedAscending) {
            Collections.reverse(shownProducts);
        }

        sortedDirectionEco = false; // Becuase calling this method always sorts disregarding eco
    }

    @FXML
    private void sortByName() {
        resetDirs();
        sortedDirectionName = true;
        shownProducts.sort(Comparator.comparing(Product::getName));
        if (sortedDirectionName) {
            if (!sortedAscending) {
                Collections.reverse(shownProducts);
            }
            sortedAscending = !sortedAscending;
        } else {
            if (sortedAscending) {
                Collections.reverse(shownProducts);
            }
        }
        changeArrowDir(nameUp, nameDown, !sortedAscending);
        updateProductList();

    }

    @FXML
    private void sortByPrice() {
        resetDirs();
        sortedDirectionPrice = true;
        shownProducts.sort(Comparator.comparing(Product::getPrice));
        if (sortedDirectionPrice) {
            if (!sortedAscending) {
                Collections.reverse(shownProducts);
            }
            sortedAscending = !sortedAscending;
        } else {
            if (sortedAscending) {
                Collections.reverse(shownProducts);
            }
        }
        changeArrowDir(priceUp, priceDown, sortedAscending);
        updateProductList();

    }


    @FXML
    private void sortByEco() {
        if (!sortedDirectionEco) {
            sortedDirectionEco = true;

            Collections.reverse(shownProducts);
            shownProducts.sort(Comparator.comparing(Product::isEcological));
            Collections.reverse(shownProducts);

            /*if (sortedAscending) {
                shownProducts.sort(Comparator.comparing(Product::isEcological));
            } else {
                shownProducts.sort(Comparator.comparing(Product::isEcological));
            }*/
            updateProductList();
        }

        /*if (sortedDirectionEco) {
            Collections.reverse(shownProducts);
            sortedAscending = !sortedAscending;
        } else {
            resetDirs();
            sortedDirectionEco = true;
            shownProducts.sort(Comparator.comparing(Product::isEcological));
            if (sortedAscending) {
                Collections.reverse(shownProducts);
            }
        }
        changeArrowDir(ecoUp, ecoDown, sortedAscending);
        updateProductList();*/

    }

    private void changeArrowDir(ImageView im1, ImageView im2, boolean bool) {
        resetArrows(); //All sorting methods are absolute, no weightning of the previous list which means we might as well reset arrows to avoid confusion

        if (bool) {
            setArrowFilled(im1);
            setArrowFaded(im2);
        } else {
            setArrowFaded(im1);
            setArrowFilled(im2);
        }
    }

    private void setArrowFilled(ImageView im) {
        im.setImage((new Image(getClass().getClassLoader().getResourceAsStream("iMat/resources/arrow_filled.png"))));
    }

    private void setArrowFaded(ImageView im) {
        im.setImage((new Image(getClass().getClassLoader().getResourceAsStream("iMat/resources/arrow_faded.png"))));
    }


    @FXML
    public void openDetailView(Product product) {
        populateDetailView(product);
        detailViewPane.toFront();
    }

    @FXML
    private void closeDetailView(){
        detailViewPane.toBack();
    }

    private void blurInSearchBar() {
        if(!searchFieldBlurred) {
            blurSearchBar.toFront();
            FadeTransition fade = new FadeTransition(Duration.seconds(0.7), blurSearchBar);
            fade.setFromValue(0);
            fade.setToValue(1.0);
            fade.setCycleCount(1);
            fade.play(); // start animation
            searchFieldBlurred = true;
        }
    }

    private void blurOutSearchBar() {
        if(searchFieldBlurred) {
            FadeTransition fade = new FadeTransition(Duration.seconds(0.7), blurSearchBar);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setCycleCount(1);
            fade.setOnFinished(new EventHandler<ActionEvent>() {    // Action after the animation is done
                @Override
                public void handle(ActionEvent event) {
                    blurSearchBar.toBack();
                }
            });
            fade.play(); // start animation
            searchFieldBlurred = false;
        }
    }

    private void resetCheckoutErrorLabels() {
        checkoutErrorLabel.setText("");
        checkoutErrorLabel2.setText("");
        deliveryTimeErrorLabel.setText("");
        resetCheckoutErrorBorders();

    }
    private void showCheckoutErrorLabels() {
        checkoutErrorLabel.setText(missingFieldText.getMessageContent());
        checkoutErrorLabel2.setText(missingFieldText.getMessageContent());
    }
    private void showDeliveryTimeErrorLabel() {
        deliveryTimeErrorLabel.setText(missingDeliveryTimeText.getMessageContent());
        checkoutDatePicker.setStyle("-fx-border-color: red");
    }
    private void resetCheckoutErrorBorders() {
        firstNameField.setStyle(null);
        lastNameField.setStyle(null);
        phoneField.setStyle(null);
        addressField.setStyle(null);
        postalCodeField.setStyle(null);
        countyField.setStyle(null);
        checkoutDatePicker.setStyle(null);

        creditCardField.setStyle(null);
        expiryMonthField.setStyle(null);
        expiryYearField.setStyle(null);
        cvcField.setStyle(null);
    }

    // Separate "finish" and "back to" methods because it matters if you are completing a step or just going back to a previous one
    @FXML
    private void toCheckout() {

        if (shownCartList.size() - (CartListItem.fadeAlertsOngoing+CartListItem.removalQueue.size()) > 0) { // Check if cart is not empty
            searchField.clear();
            blurInSearchBar();
            checkoutView1.toFront();
            Platform.runLater( () -> checkoutCartScrollPane.requestFocus());
            inCheckout = true;
            updateCheckoutCart();


        } else {
            populateMessageView(emptyCartMessage);
            showMessage();

        }
    }

    @FXML
    private void finishCheckoutStep1() {
        if (shownCartList.size() - (CartListItem.fadeAlertsOngoing+CartListItem.removalQueue.size()) > 0) { // Check if cart is not empty
            checkoutView2.toFront();
            autoFill(); // fill the text fields in step 2 automatically
            resetCheckoutErrorLabels();
        } else {
            populateMessageView(emptyCartMessage);
            showMessage();
        }
    }

    @FXML
    private void finishCheckoutStep2() {
        if (checkAllAddressFields() && deliveryTimeIsSelected()) {
            bc.getCustomer().setFirstName(firstNameField.getText());
            bc.getCustomer().setLastName(lastNameField.getText());
            bc.getCustomer().setAddress(addressField.getText());
            bc.getCustomer().setPostCode(postalCodeField.getText());
            bc.getCustomer().setPostAddress(countyField.getText());
            bc.getCustomer().setPhoneNumber(phoneField.getText());
            resetCheckoutErrorLabels();

            if (bc.isCustomerComplete()) {
                checkoutView3.toFront();
                placeOrderButton.setText("Betala " + cart.getTotal() + " kr");
            }
        }
    }

    @FXML
    private void finishCheckout() {
        if (checkAllCreditCardFields()) {
            bc.getCreditCard().setCardNumber(creditCardField.getText());
            bc.getCreditCard().setValidMonth(Integer.parseInt(expiryMonthField.getText()));
            bc.getCreditCard().setValidYear(Integer.parseInt(expiryYearField.getText()));
            bc.getCreditCard().setVerificationCode(Integer.parseInt(cvcField.getText()));

            int numberofItems = 0;
            for (int i = 0; i < cart.getItems().size(); i++) {
                numberofItems += cart.getItems().get(i).getAmount();
            }
            //int numberofItems = cart.getItems().size();
            double totalprice = cart.getTotal();
            Message orderMessage = new Message("Din order är slutförd! Tack för din beställning!",
                    "Sammanfattning av order:"
                            + "\n\nAntal varor: " + numberofItems + " st"
                            + "\nTotalpris: " + totalprice + " kr"
                            + "\nAdress: " + bc.getCustomer().getAddress()
                            + "\nLeveranstid: " + checkoutDatePicker.getValue() + ",   kl " + deliveryTime
                            + "\nTelefonnummer: " + bc.getCustomer().getPhoneNumber());
            populateMessageView(orderMessage);
            bc.placeOrder(); // Saves the order placement and clears the shopping cart
            clearFields();
            updateCartTotal();
            creditCardField.setText("");
            expiryMonthField.setText("");
            expiryYearField.setText("");
            cvcField.setText("");
            blurOutSearchBar();
            mainView.toFront();
            showMessage();
            inCheckout = false;
        }
    }

    @FXML
    private void backToCheckoutStep1() {
        checkoutView1.toFront();
        Platform.runLater( () -> checkoutCartScrollPane.requestFocus());
    }

    @FXML
    private void backToCheckoutStep2() {
        checkoutView2.toFront();
        resetCheckoutErrorLabels();
    }

    @FXML
    private void goHome() {
        blurOutSearchBar();
        mainView.toFront();
        inCheckout = false;
        allCategoryButton.setSelected(true); // Reset category
        searchField.setText(""); // Reset search
        updateCurrentCategory();
        updateCartList();
    }

    @FXML
    private void closePurchaseHistory() {
        if (!inCheckout) {
            blurOutSearchBar();
        }
        purchaseHistoryPane.toBack();
    }

    @FXML
    private void closeHelp() {
        if (!inCheckout) {
            blurOutSearchBar();
        }
        helpPane.toBack();
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
        cartTotalLabel.setText("Totalkostnad: " + (double)(int)(cart.getTotal()*100) / 100 + " kr");

        int numberOfItems = 0;
        for (ShoppingItem si : cart.getItems()) {
            numberOfItems += si.getAmount();
        }
        cartAmountLabel.setText(numberOfItems + " st varor");

    }

    void updateCheckoutTotal() {
        checkoutTotalLabel.setText("Totalkostnad: " + (double)(int)(cart.getTotal()*100) / 100 + " kr");

    }

    @FXML
    void emptyCart(){
        cart.clear();
        oldCartList.clear();
        shownCartList.clear();
        updateCartTotal();
        updateCartList();
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
        creditCardField.setText(bc.getCreditCard().getCardNumber());
        expiryMonthField.setText(String.valueOf(bc.getCreditCard().getValidMonth()));
        expiryYearField.setText(String.valueOf(bc.getCreditCard().getValidYear()));
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
        return (
                checkField(firstNameField) &
                        checkField(lastNameField) &
                        checkField(addressField) &
                        checkField(postalCodeField) &
                        checkField(countyField) &
                        checkField(phoneField)
        );
    }

    private boolean checkAllCreditCardFields() {
        return (
                checkField(creditCardField) &
                        checkField(expiryMonthField) &
                        checkField(expiryYearField) &
                        checkField(cvcField)
        );
    }

    private boolean checkField(TextField t) {
        if (t.getText().equals("")) {
            missingTextAlert(t);
            showCheckoutErrorLabels();
            return false;
        }
        return true;
    }

    private boolean deliveryTimeIsSelected() {
        if (checkoutDatePicker.getValue() == null || deliveryTime.equals("")) {
            showDeliveryTimeErrorLabel();
            return false;
        }
        return true;
    }

    private boolean addressFieldsAreFilled() {
        return (
                !firstNameField.equals("") &
                        !lastNameField.equals("") &
                        !addressField.equals("") &
                        !postalCodeField.equals("") &
                        !countyField.equals("") &
                        !phoneField.equals("")
        );
    }

    private boolean creditCardFieldsAreFilled() {
        return (
                !creditCardField.equals("") &
                        !expiryMonthField.equals("") &
                        !expiryYearField.equals("") &
                        !cvcField.equals("")
        );
    }


    private void missingTextAlert(TextField t) {
        t.setStyle("-fx-border-color: red");

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), t);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setCycleCount(4); //the amount of times the animation plays (fade in + out = 2)
        fade.setAutoReverse(true);
        fade.play(); // start animation

    }

    private void populateMessageView(Message m) {
        messageName.setText(m.getMessageTitle());
        messageField.setText(m.getMessageContent());
    }

    private void showMessage() {
        messagePane.toFront();
    }

    @FXML
    private void moveMessagePaneBack() {
        messagePane.toBack();
    }


    @FXML
    public void closeButtonMouseEnteredMessage() {
        messageExitIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressedMessage() {
        messageExitIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExitedMessage() {
        messageExitIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close.png")));
    }

    @FXML
    public void closeButtonMouseEntered() {
        exitIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed() {
        exitIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited() {
        exitIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close.png")));
    }


    @FXML
    public void logoButtonMouseEntered() {
        logoImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/imat_icon_hover.gif")));
    }

    @FXML
    public void logoButtonMouseExited() {
        logoImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/imat_icon.gif")));
    }
    @FXML
    public void mouseTrap(Event event) {
        event.consume();
    }

    /**
     * Custom tooltip that shows up instantly instead of a big delay with Tooltip.install(Node node, Tooltip t);
     */
    public static void bindTooltip(final Node node, final Tooltip tooltip){
        node.setOnMouseMoved(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                // Y + 15 moves the tooltip 15 pixels below the mouse cursor
                tooltip.show(node, event.getScreenX() + 10, event.getScreenY() + 10);
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                tooltip.hide();
            }
        });
    }

}

