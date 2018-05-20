package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class PurchaseHistoryProductListItem extends AnchorPane {

    private Controller parentController;
    private ShoppingItem item;

    @FXML
    private ImageView productImageView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label numberofItemsLabel;
    @FXML
    private Label priceLabel;

    PurchaseHistoryProductListItem(Controller controller, ShoppingItem item) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("purchasehistoryproduct_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = controller;
        this.item = item;

        //Image setup
        productImageView.setImage(controller.getSquareImage(controller.getProductImage(item.getProduct())));

        //Text setup
        nameLabel.setText(item.getProduct().getName());
        priceLabel.setText((double)(int)(item.getTotal() * 100)/100 + " kr"); // Limits to 2 decimals (otherwise it sometimes shows lots of zeros for no reason)
        numberofItemsLabel.setText(item.getAmount() + " st");


    }
}
