package iMat;

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
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

class ProductListItem extends AnchorPane {
    private Controller parentController;
    private Product product;
    private int amount = 1; //Initialize to 1 because you never want to add 0 items to cart of a specific product

    @FXML private ImageView productImageView;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private Button plusButton;
    @FXML private Button minusButton;
    @FXML private Button buyButton;
    @FXML private TextField quantityTextField;
    @FXML private ImageView favoriteItemImageView;
    @FXML private SplitPane productSplitPane;
    @FXML private ImageView ecoImageView;


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

        //Image setup
        productImageView.setImage(controller.getSquareImage(controller.getProductImage(product)));

        //Text setup
        nameLabel.setText(product.getName());
        priceLabel.setText(product.getPrice() + " kr");
        quantityTextField.setText(amount + " st");
        productSplitPane.setDividerPositions(0,0,0);

    }


    @FXML
    protected void purchaseItem(){
        parentController.purchaseItem(this.product, this.amount);
        parentController.updateCartTotal();
        amount=1;
        quantityTextField.setText(amount + " st");
    }

    /**
     * Checks if star on product is clicked and sets according status
     */
    @FXML
    protected void favIconOnClick(Event event) {
        if (!parentController.getFavStatus(this.product)){
            parentController.addToFavorites(this.product);
        }
        else{
            parentController.removeFromFavorites(this.product);
        }
        parentController.updateFavImage();

    }

    @FXML
    private void incAmount(){
        amount++;
        quantityTextField.setText(amount + " st");
    }

    @FXML
    private void decAmount(){
        amount--;
        quantityTextField.setText(amount + " st");
    }


    /**
     * Hints when mouse enters the star
     */
    @FXML
    public void favIconMouseEntered(){
        this.favoriteItemImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMat/resources/favorite_item_hint.png")));
    }

    @FXML
    public void favIconMouseExited(){
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