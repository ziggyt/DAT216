package iMat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;

import java.io.File;
import java.io.IOException;

import static java.lang.Integer.parseInt;

class ProductListItem extends AnchorPane {
    private Controller parentController;
    private Product product;
    private int amount = 1; //Initialize to 1 because you never want to add 0 items to cart of a specific product
    private boolean favClicked; // For deciding when to show what fav image

    private final static Image favorite_item_hint = new Image("iMat/resources/favorite_item_hint.png");

    @FXML
    private ImageView productImageView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Button infoButton;
    @FXML
    private Button plusButton;
    @FXML
    private Button minusButton;
    @FXML
    private Button buyButton;
    @FXML
    private TextField quantityTextField;
    @FXML
    private ImageView favoriteItemImageView;
    @FXML
    private ImageView ecoImageView;


    ProductListItem(Product product, Controller controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;
        this.parentController = controller;

        // Image setup
        productImageView.setImage(controller.getSquareImage(controller.getProductImage(product)));

        // Text setup
        nameLabel.setText(product.getName());
        priceLabel.setText(product.getPrice() + " " + product.getUnit());
        quantityTextField.setText(amount + "");

        // Disable the minus button when it does nothing
        minusButton.setDisable(true);

        // Forces the field to be numeric only and limits to 2 digits
        quantityTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Check for non numeric characters
                if (!newValue.matches("\\d*")) {
                    // Remove all non numeric characters
                    quantityTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                // Check if text is set to longer than 2
                if (quantityTextField.getText().length() > 2) {
                    // Set length to 99
                    quantityTextField.setText("99");
                }
                // Update amount
                if (!quantityTextField.getText().equals("") && Integer.parseInt(quantityTextField.getText()) > 0) {
                    amount = Integer.parseInt(quantityTextField.getText());
                } else {
                    quantityTextField.setText("");
                }

                // Disable the plus and minus buttons when they do nothing
                if (amount == 1) {
                    plusButton.setDisable(false);
                    if (minusButton.isFocused()) {
                        plusButton.requestFocus();
                    }
                    minusButton.setDisable(true);
                } else if (amount == 99) {
                    minusButton.setDisable(false);
                    if (plusButton.isFocused()) {
                        minusButton.requestFocus();
                    }
                    plusButton.setDisable(true);
                } else {
                    minusButton.setDisable(false);
                    plusButton.setDisable(false);
                }
            }
        });
    }


    @FXML
    protected void openDetailView() {
        parentController.openDetailView(product);
    }

    @FXML
    protected void purchaseItem() {
        parentController.purchaseItem(this.product, this.amount);
        parentController.updateCartTotal();
        amount = 1;
        quantityTextField.setText(amount + " st");
    }

    @FXML
    private void incAmount() {
        if (amount < 99) {
            amount++;
        }
        quantityTextField.setText(amount + " st");

        // Disable the plus and minus buttons when they do nothing
        if (amount == 1) {
            plusButton.setDisable(false);
            if (minusButton.isFocused()) {
                plusButton.requestFocus();
            }
            minusButton.setDisable(true);
        } else if (amount == 99) {
            minusButton.setDisable(false);
            if (plusButton.isFocused()) {
                minusButton.requestFocus();
            }
            plusButton.setDisable(true);
        } else {
            minusButton.setDisable(false);
            plusButton.setDisable(false);
        }
    }

    @FXML
    private void decAmount() {
        if (amount > 1) {
            amount--;
        }
        quantityTextField.setText(amount + " st");

        // Disable the plus and minus buttons when they do nothing
        if (amount == 1) {
            plusButton.setDisable(false);
            if (minusButton.isFocused()) {
                plusButton.requestFocus();
            }
            minusButton.setDisable(true);
        } else if (amount == 99) {
            minusButton.setDisable(false);
            if (plusButton.isFocused()) {
                minusButton.requestFocus();
            }
            plusButton.setDisable(true);
        } else {
            minusButton.setDisable(false);
            plusButton.setDisable(false);
        }
    }


    /**
     * Checks if star on product is clicked and sets according status
     */
    @FXML
    protected void favIconOnClick(Event event) {
        favClicked = true;
        if (!parentController.getFavStatus(this.product)) {
            parentController.addToFavorites(this.product);
        } else {
            parentController.removeFromFavorites(this.product);
        }
        parentController.updateFavImage();
    }

    /**
     * Hints when mouse enters the star
     */
    @FXML
    public void favIconMouseEntered() {
        if (!favClicked) {
            this.favoriteItemImageView.setImage(favorite_item_hint);
        }
        favClicked = false;
    }

    @FXML
    public void favIconMouseExited() {
        parentController.updateFavImage();
    }

    /**
     * Updates the ImageViews Image property
     */
    public void setFavoriteItemImage(Image favoriteItemImage) {
        this.favoriteItemImageView.setImage(favoriteItemImage);
    }

    public void setEcoImage(Image ecoImage) {
        this.ecoImageView.setImage(ecoImage);
    }
}