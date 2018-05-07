package iMat;

import javafx.event.Event;
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

class ProductListItem extends AnchorPane {
    private Controller parentController;
    private Product product;

    @FXML private ImageView productImageView;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private Button buyButton;
    @FXML private ImageView favoriteItemImage;

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
    }


    @FXML
    protected void purchaseItem(){
        parentController.getBc().getShoppingCart().addItem(new ShoppingItem(product, 1));
    }

    /**
     * Checks if star on product is clicked and sets according status
     */
    @FXML
    protected void favIconOnClick(Event event) {
        if (!parentController.getFavStatus(product)){
            parentController.addToFavorites(this.product);
        }
        else{
            parentController.removeFromFavorites(this.product);
        }
        parentController.updateFavImage();

    }


    /**
     * Hints when mouse enters the star
     */
    @FXML
    public void favIconMouseEntered(){
        this.favoriteItemImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
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
        this.favoriteItemImage.setImage(favoriteItemImage);
    }
}