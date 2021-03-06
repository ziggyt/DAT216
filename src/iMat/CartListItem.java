package iMat;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;


public class CartListItem extends AnchorPane {

    private Controller parentController;
    private ShoppingItem shoppingItem;

    @FXML
    private ImageView productImageView;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField quantityTextField;
    @FXML
    private Button plusButton;
    @FXML
    private Button minusButton;
    @FXML
    private Label priceLabel;
    @FXML
    private ImageView closeIconImageView;
    @FXML
    private AnchorPane fadePane;
    @FXML
    private AnchorPane addedFadePane;
    @FXML
    private AnchorPane removedFadePane;
    @FXML
    private AnchorPane greyPane;

    private FadeTransition fade = new FadeTransition(); // Needs to be instantiated to be able to call fade.stop() when we don't know if it has started yet
    private FadeTransition addedFade;
    private FadeTransition removedFade;

    static int fadeAlertsOngoing = 0; // Number of items currently in the fadeAlert animation
    static List<ShoppingItem> removalQueue = new ArrayList<>(); // List of items waiting to get removed

    CartListItem(ShoppingItem si, Controller controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cart_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.shoppingItem = si;
        this.parentController = controller;

        //Image setup
        productImageView.setImage(controller.getSquareImage(controller.getProductImage(si.getProduct())));

        //Text setup
        nameLabel.setText(si.getProduct().getName());
        priceLabel.setText((double)(int)(shoppingItem.getTotal() * 100)/100 + " kr"); // Limits to 2 decimals (otherwise it sometimes shows lots of zeros for no reason)
        quantityTextField.setText((int) si.getAmount() + "");

        // Disable the plus and minus buttons when they do nothing
        if (si.getAmount() == 1) {
            plusButton.setDisable(false);
            if (minusButton.isFocused()) {
                plusButton.requestFocus(); // When a the minusButton is disabled the focus is moved to the next element which could look weird
            }
            minusButton.setDisable(true);
        } else if (si.getAmount() == 99) {
            minusButton.setDisable(false);
            if (plusButton.isFocused()) {
                minusButton.requestFocus();
            }
            plusButton.setDisable(true);
        } else {
            minusButton.setDisable(false);
            plusButton.setDisable(false);
        }

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
                    // Set amount to 99
                    quantityTextField.setText("99");
                }
                try {
                    if (Integer.parseInt(oldValue) > 99) {
                        oldValue = "99"; // oldValue needs to be correctly adjusted to 99 if it was larger
                    }
                } catch (NumberFormatException e) {}
                try {
                    if (Integer.parseInt(quantityTextField.getText()) > Integer.parseInt(oldValue)) {

                        // Handle if the item was in the process of getting removed and the purchase button gets pressed in the ProductListItem
                        if (removalQueue.contains(shoppingItem)) {
                            removalQueue.remove(shoppingItem);
                        }
                        fade.setOnFinished(new EventHandler<ActionEvent>() { // Replace the on finished method (only relevant if a fade was currently playing)
                            @Override
                            public void handle(ActionEvent event) {
                                fadeAlertsOngoing--;
                                if (fadeAlertsOngoing <= 0) { // If there are no other items currently in the fadeAlert animation
                                    clearRemovalQueue(); // Remove everything in the queue
                                }
                            }
                        });

                        addedFadeAlert(); // Green flash to confirm the added item

                    } else if (Integer.parseInt(quantityTextField.getText()) < parseInt(oldValue) && Integer.parseInt(quantityTextField.getText()) != 0) {
                        removedFadeAlert(); // Red flash to confirm the removed item
                    }
                } catch (NumberFormatException e) {}

                // Update amount
                try {
                    if (Integer.parseInt(quantityTextField.getText()) > 0) {
                        shoppingItem.setAmount(Integer.parseInt(quantityTextField.getText()));
                    } else {
                        quantityTextField.setText("");
                    }
                } catch (NumberFormatException e) {
                    quantityTextField.setText("");
                }

                // Update price labels
                priceLabel.setText((double)(int)(shoppingItem.getTotal() * 100)/100 + " kr"); // Limits to 2 decimals (otherwise it sometimes shows lots of zeros for no reason)
                parentController.updateCartTotal();
                parentController.updateCheckoutTotal();

                // Disable the plus and minus buttons when they do nothing
                if (shoppingItem.getAmount() == 1) {
                    plusButton.setDisable(false);
                    if (minusButton.isFocused()) {
                        plusButton.requestFocus();
                    }
                    minusButton.setDisable(true);
                } else if (shoppingItem.getAmount() == 99) {
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

        addedFadeAlert();   // Green flash to confirm the added item
    }

    public void updateQuantityTextField() {
        quantityTextField.setText((int) shoppingItem.getAmount() + "");
    }

    @FXML
    protected void incAmount() {
        if (shoppingItem.getAmount() < 99) {
            shoppingItem.setAmount(shoppingItem.getAmount() + 1);
        }
        updateQuantityTextField();

        // Disable the plus and minus buttons when they do nothing
        if (shoppingItem.getAmount() == 1) {
            plusButton.setDisable(false);
            if (minusButton.isFocused()) {
                plusButton.requestFocus();
            }
            minusButton.setDisable(true);
        } else if (shoppingItem.getAmount() == 99) {
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
    protected void decAmount() {
        if (shoppingItem.getAmount() > 1) {
            shoppingItem.setAmount(shoppingItem.getAmount() - 1);
        }
        updateQuantityTextField();

        // Disable the plus and minus buttons when they do nothing
        if (shoppingItem.getAmount() == 1) {
            plusButton.setDisable(false);
            if (minusButton.isFocused()) {
                plusButton.requestFocus();
            }
            minusButton.setDisable(true);
        } else if (shoppingItem.getAmount() == 99) {
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
    protected void openDetailView() {
        parentController.openDetailView(shoppingItem.getProduct());
    }

    @FXML
    public void closeIconMouseEntered() {
        this.closeIconImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeIconMouseExited() {
        this.closeIconImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/icon_close.png")));
    }

    @FXML
    private void removeItem() {
        fadePane.toFront();
        fadeAlert();
    }


    @FXML
    private void regret() {  //Rearranges the panes to show a grey background again and stops animation
        fadePane.toBack();
        fade.stop();
        if (removalQueue.contains(shoppingItem)) {
            removalQueue.remove(shoppingItem); // Remove this item from the removalQueue
        } else { // If the animation didn't finish before regretting
            fadeAlertsOngoing--;
            if (fadeAlertsOngoing <= 0) { // If this was the last ongoing animation
                clearRemovalQueue(); // Make sure the items in removalQueue gets removed
            }
        }
    }

    /**
     * Animation when item gets removed, with regret option
     * If there are multiple items in the process of getting removed, sync the animations so that they all finish at the same time
     */
    private void fadeAlert() {
        fadeAlertsOngoing++;
        fade = new FadeTransition(Duration.seconds(0.15), fadePane);
        fade.setFromValue(0.0); //From almost solid to completely solid,
        fade.setToValue(0.8);
        fade.setCycleCount(1);

        fade.setOnFinished(new EventHandler<ActionEvent>() {    // Action after the animation is done
            @Override
            public void handle(ActionEvent event) {
                fadeAlertContinuation(); // Continue the animation
            }
        });

        fade.play(); // start animation
    }
    // Continuation of fadeAlert as if they were one
    private void fadeAlertContinuation() {
        fade = new FadeTransition(Duration.seconds(4), fadePane);
        fade.setFromValue(0.8); //From almost solid to completely solid,
        fade.setToValue(1.0);
        fade.setCycleCount(1);

        fade.setOnFinished(new EventHandler<ActionEvent>() {    // Action after the animation is done
            @Override
            public void handle(ActionEvent event) {
                fadeAlertsOngoing--;
                if (fadeAlertsOngoing > 0) { // If there are other items currently in the fadeAlert animation
                    removalQueue.add(shoppingItem); // Add this item to the queue for removal
                } else {
                    removalQueue.add(shoppingItem); // Include this item
                    clearRemovalQueue(); // Remove everything in the queue
                }
            }
        });

        fade.play(); // start animation
    }

    private void clearRemovalQueue() {
        for (ShoppingItem si : removalQueue) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    parentController.removeItemFromCart(si);  // Remove item from cart
                    removalQueue.remove(si); // Clean up removalQueue
                }
            });
        }
    }

    private void addedFadeAlert() {
        addedFadePane.toFront();
        greyPane.toFront();
        addedFade = new FadeTransition(Duration.seconds(0.25), greyPane);
        addedFade.setFromValue(1.0);
        addedFade.setToValue(0.3);
        addedFade.setCycleCount(2);
        addedFade.setAutoReverse(true);
        addedFade.play();

    }

    // For when reducing the amount
    private void removedFadeAlert() {
        removedFadePane.toFront();
        greyPane.toFront();
        removedFade = new FadeTransition(Duration.seconds(0.25), greyPane);
        removedFade.setFromValue(1.0);
        removedFade.setToValue(0.5);
        removedFade.setCycleCount(2);
        removedFade.setAutoReverse(true);
        removedFade.play();
    }

    /**
     * Updates the ImageViews Image property
     */
    public void setFavoriteItemImage(Image favoriteItemImage) {
        this.closeIconImageView.setImage(favoriteItemImage);
    }

}