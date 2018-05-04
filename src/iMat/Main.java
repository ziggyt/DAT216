package iMat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("iMat.fxml"));
        primaryStage.setTitle("iMat");
        primaryStage.setScene(new Scene(root, 1280, 880));
        primaryStage.getIcons().add(new Image("https://raw.githubusercontent.com/ziggyt/DAT216/master/imat_icon.gif"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
