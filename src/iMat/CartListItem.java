package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

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
        priceLabel.setText(si.getTotal() + " kr");
        quantityLabel.setText(si.getAmount() + " st");
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
        parentController.removeItemFromCart(this.shoppingItem);
    }


    /**
     * Updates the ImageViews Image property
     */
    public void setFavoriteItemImage(Image favoriteItemImage) {
        this.closeIconImageView.setImage(favoriteItemImage);
    }

}