package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DefineNewCategoryController {
    @FXML
    private TextField categoryName;
    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    private void showHomePage(ActionEvent event) {
        try {
            // Load the new page FXML file
            root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveButtonClick() {
        // Check if the text field is empty
        if (categoryName.getText().trim().isEmpty()) {
            // Text field is empty, show the alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a category name.");
            alert.showAndWait();
        } else {
            try {
                // Create a new CSV file if it doesn't exist
                File file = new File("categories.csv");
                if (!file.exists()) {
                    file.createNewFile();
                }

                // Write the input text to the CSV file
                FileWriter csvWriter = new FileWriter(file, true);
                csvWriter.append(categoryName.getText() + "\n");
                csvWriter.flush();
                csvWriter.close();

                // Display a message to indicate successful saving
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Information");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Category name \"" + categoryName.getText() + "\" has been saved!");
                successAlert.showAndWait();
                categoryName.clear(); // Clear the text from the TextField
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file writing error
            }
        }
    }
}
