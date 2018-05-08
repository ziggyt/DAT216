package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class RegisterListItem extends AnchorPane {

    private Controller parentController;
    private Product product;
    private int amount = 1;

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


    RegisterListItem(Product product, Controller controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;
        this.parentController = controller;

        //Image setup
        productImageView.setImage(controller.getSquareImage(controller.getProductImage(product)));

        //Text setup
        nameLabel.setText(product.getName());
        quantityLabel.setText(amount + " st");
        priceLabel.setText(product.getPrice() + " kr");
    }

    @FXML
    protected void incAmount(){
        amount++;
        quantityLabel.setText(amount + " st");
        priceLabel.setText(amount * product.getPrice() + " kr");
        parentController.updateRegisterOnChange(product, amount);
    }

    @FXML
    protected void decAmount(){
        if(amount>1){
            amount--;
        }
        quantityLabel.setText(amount + " st");
        priceLabel.setText(amount * product.getPrice() + " kr");
        parentController.updateRegisterOnChange(product, amount);
    }

    public void resetAmount(){
        amount=1;
        quantityLabel.setText(amount + " st");
        priceLabel.setText(amount * product.getPrice() + " kr");

    }

    public void amountChanged(int amount){
        this.amount = amount;
        quantityLabel.setText(amount + " st");
        priceLabel.setText(amount * product.getPrice() + " kr");

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
            parentController.removeItemFromCart(this.product);
        }


    /**
     * Updates the ImageViews Image property
     */
    public void setFavoriteItemImage(Image favoriteItemImage) {
        this.closeIconImageView.setImage(favoriteItemImage);
    }

}