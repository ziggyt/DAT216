package iMat;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;


import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CartListItem extends AnchorPane {

    private Controller parentController;
    private ShoppingItem shoppingItem;

    @FXML
    private ImageView productImageView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label quantityLabel;
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
    private AnchorPane greyPane;
    @FXML
    private Button regretButton;



    private FadeTransition fade;



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
        priceLabel.setText(Math.round(si.getTotal()) + " kr");
        quantityLabel.setText((int)si.getAmount() + " st");
    }

    @FXML
    protected void incAmount(){
        shoppingItem.setAmount(shoppingItem.getAmount()+1);
        priceLabel.setText(Math.round(shoppingItem.getTotal()) + " kr");
        quantityLabel.setText((int)shoppingItem.getAmount() + " st");
        parentController.updateCartTotal();
    }

    @FXML
    protected void decAmount(){
        if(shoppingItem.getAmount()>1){
            shoppingItem.setAmount(shoppingItem.getAmount()-1);
        }
        priceLabel.setText(Math.round(shoppingItem.getTotal()) + " kr");
        quantityLabel.setText((int)shoppingItem.getAmount() + " st");
        parentController.updateCartTotal();
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
        //parentController.removeItemFromCart(this.shoppingItem); //right now the item is not removed, we need to implement some sort of timer for removal
    }


    @FXML
    private void regret(){  //Rearranges the panes to show a grey background again and stops animation
        greyPane.toBack();
        regretButton.toBack();
        fadePane.toBack();
        fade.stop();

    }

    private void fadeAlert(){
        fade = new FadeTransition(Duration.seconds(4), fadePane); //It will play animation for 4 seconds
        fade.setFromValue(0.0); //the pane is invisible to start
        fade.setToValue(0.9); //fades in to almost completely solid
        fade.setCycleCount(4); //the amount of cycles during a set period of time (during 4 seconds it will fade in and fade out 4 times in this case
        fade.setAutoReverse(true); //in order to make it go from solid to transparent (i think)
        fade.play(); // start animation

    }

    /**
     * Updates the ImageViews Image property
     */
    public void setFavoriteItemImage(Image favoriteItemImage) {
        this.closeIconImageView.setImage(favoriteItemImage);
    }

}