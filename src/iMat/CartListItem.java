package iMat;

import javafx.animation.FadeTransition;
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
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;


import javax.swing.*;
import javax.xml.soap.Text;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CartListItem extends AnchorPane {

    private Controller parentController;
    private ShoppingItem shoppingItem;

    @FXML private ImageView productImageView;
    @FXML private Label nameLabel;
    @FXML private TextField quantityTextField;
    @FXML private Button plusButton;
    @FXML private Button minusButton;
    @FXML private Label priceLabel;
    @FXML private ImageView closeIconImageView;
    @FXML private AnchorPane fadePane;
    @FXML private AnchorPane addedFadePane;
    @FXML private AnchorPane greyPane;
    @FXML private Button regretButton;

    private FadeTransition fade;
    private FadeTransition addedFade;

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
        priceLabel.setText((si.getTotal()) + " kr");
        quantityTextField.setText((int)si.getAmount() + "");

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
                if (quantityTextField.getText().length() >= 2) {
                    // Set length to 2
                    quantityTextField.setText(quantityTextField.getText().substring(0, 2));
                }
                addedFadeAlert();   // Green flash to confirm the added item
                // Update amount
                shoppingItem.setAmount(Integer.parseInt(quantityTextField.getText()));
                // Update price labels
                priceLabel.setText((shoppingItem.getTotal()) + " kr");
                parentController.updateCartTotal();
                parentController.updateCheckoutTotal();

            }
        });

        addedFadeAlert();   // Green flash to confirm the added item
    }

    public void updateQuantityTextField() {
        quantityTextField.setText((int)shoppingItem.getAmount() + "");
    }

    @FXML
    protected void incAmount(){
        if(shoppingItem.getAmount()<99) {
            shoppingItem.setAmount(shoppingItem.getAmount() + 1);
        }
        updateQuantityTextField();
    }

    @FXML
    protected void decAmount(){
        if(shoppingItem.getAmount()>1){
            shoppingItem.setAmount(shoppingItem.getAmount()-1);
        }
        updateQuantityTextField();
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
    private void removeItem(){
        fadePane.toFront();
        regretButton.toFront(); //Button cant belong to fadePane because it will get faded in and out
        fadeAlert();
    }


    @FXML
    private void regret(){  //Rearranges the panes to show a grey background again and stops animation
        regretButton.toBack();
        fadePane.toBack();
        fade.stop();
    }

    private void fadeAlert(){
        fade = new FadeTransition(Duration.seconds(1), fadePane); //It will play animation for 1 second
        fade.setFromValue(0.0); //the pane is invisible to start
        fade.setToValue(0.9); //fades in to almost completely solid
        fade.setCycleCount(4); //the amount of times the animation plays (fade in + out = 2)
        fade.setAutoReverse(true); //in order to make it go from solid to transparent
        fade.setOnFinished(new EventHandler<ActionEvent>() {    // Action after the animation is done
            @Override
            public void handle(ActionEvent event) {
                parentController.removeItemFromCart(shoppingItem);  // Remove item from cart after the animation
            }
        });
        fade.play(); // start animation

    }

    private void addedFadeAlert(){
        addedFadePane.toFront();
        greyPane.toFront();
        addedFade = new FadeTransition(Duration.seconds(0.25), greyPane);
        addedFade.setFromValue(1.0);
        addedFade.setToValue(0.2);
        addedFade.setCycleCount(2);
        addedFade.setAutoReverse(true);
        addedFade.play();

    }

    /**
     * Updates the ImageViews Image property
     */
    public void setFavoriteItemImage(Image favoriteItemImage) {
        this.closeIconImageView.setImage(favoriteItemImage);
    }

}