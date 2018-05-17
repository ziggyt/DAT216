package iMat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se.chalmers.cse.dat216.project.IMatDataHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("iMat.fxml"));

        primaryStage.setTitle("iMat");
        primaryStage.setScene(new Scene(root, 1280, 880));
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(880);
        primaryStage.setResizable(false);

        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("iMat/resources/imat_icon.gif")));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
        //IMatDataHandler.getInstance().reset();
    }


    public static void main(String[] args) {

        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                IMatDataHandler.getInstance().shutDown();
            }
        }));
    }
}
