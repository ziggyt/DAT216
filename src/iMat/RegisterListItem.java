package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;

public class RegisterListItem {

    private Controller parentController;
    private Product product;

    @FXML private ImageView productImageView;
    @FXML private Label nameLabel;
    @FXML private Label quantityLabel;
    @FXML private Button plusButton;
    @FXML private Button minusButton;
    @FXML private Label priceLabel;
    @FXML private ImageView closeIconImageView;


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
        priceLabel.setText(product.getPrice() + " kr");
    }

}
