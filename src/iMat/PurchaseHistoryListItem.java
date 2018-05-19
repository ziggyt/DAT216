package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;

import java.io.IOException;

public class PurchaseHistoryListItem extends AnchorPane {

    private Controller parentController;
    private Order order;

    @FXML
    private Label dateLabel;
    @FXML
    private Label numberofItemsLabel;
    @FXML
    private Label priceLabel;



    PurchaseHistoryListItem(Order order, Controller controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("purchasehistory_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.order = order;
        this.parentController = controller;

        //dateLabel.setText(order.getDate().toString());

        int numberofItems = 0;
        for (int i = 0; i < order.getItems().size(); i++) {
            numberofItems += order.getItems().get(i).getAmount();
        }
        numberofItemsLabel.setText(numberofItems + " varor");

        int totalPrice = 0;
        for (int i = 0; i < order.getItems().size(); i++) {
            totalPrice += order.getItems().get(i).getTotal();
        }
        priceLabel.setText(totalPrice + " kr");

    }




}
