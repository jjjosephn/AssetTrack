package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExpiredWarranties.fxml"));
            root = loader.load();

            //run thee expired warranties controller first, if there aren't any expired warranties, then display the main page
            ExpiredWarrantiesController expiredWarrantiesController = loader.getController();
            if (expiredWarrantiesController.hasExpiredWarranties() == true) {
                expiredWarrantiesController.showExpiredWarrantiesAlert();
            } else {
                showMain(primaryStage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMain(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Home");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
