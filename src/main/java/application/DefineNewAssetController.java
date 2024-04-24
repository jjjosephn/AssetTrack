package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DefineNewAssetController implements Initializable {
    @FXML
    private TextField assetName;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private ChoiceBox<String> locationChoiceBox;
    @FXML
    private DatePicker purchaseDate;
    @FXML
    private TextField purchasedValue;
    @FXML
    private DatePicker warrantyExpiration;
    @FXML
    private TextField description;
    @FXML
    private List<String> categories = new ArrayList<>();
    @FXML
    private List<String> locations = new ArrayList<>();

    private Parent root;
    private Scene scene;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate category choice box with categories from the CSV file
        categoriesFromCSV("categories.csv");
        categoryChoiceBox.getItems().addAll(categories);
        locationsFromCSV("storages.csv");
        locationChoiceBox.getItems().addAll(locations);
    }

    private void categoriesFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                categories.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void locationsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                locations.add(details[0].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveButtonClick() {
        // Check if required fields are empty
        if (assetName.getText().trim().isEmpty() || categoryChoiceBox.getValue() == null || locationChoiceBox.getValue() == null) {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter all required fields.");
            alert.showAndWait();
        } else if (!isValid(purchaseDate.getValue().toString())) {
            System.out.println(purchaseDate.getValue().toString());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid purchase date in the format 'M/d/yyyy'.");
            alert.showAndWait();
        } else {
            try {
                // Create a new CSV file if it doesn't exist
                File file = new File("assets.csv");
                if (!file.exists()) {
                    file.createNewFile();
                }

                //If any of the optional boxes aren't filled
                String descriptionNull = description.getText() != null ? description.getText() : "";
                String purchaseValueNull = purchasedValue.getText() != null ? purchasedValue.getText() : "";
                String purchasedDateNull = purchaseDate.getValue() != null ? purchaseDate.getValue().toString() : "";
                String warrantyNull = warrantyExpiration.getValue() != null ? warrantyExpiration.getValue().toString() : "";

                // Write the input text to the CSV file
                FileWriter csvWriter = new FileWriter(file, true);
                csvWriter.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                        assetName.getText(),
                        categoryChoiceBox.getValue(),
                        locationChoiceBox.getValue(),
                        purchasedDateNull,
                        descriptionNull,
                        purchaseValueNull,
                        warrantyNull
                ));
                csvWriter.flush();
                csvWriter.close();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Information");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Asset info has been saved!");
                successAlert.showAndWait();
                    // Clear the input fields
                assetName.clear();
                categoryChoiceBox.setValue(null);
                locationChoiceBox.setValue(null);
                   description.clear();
                   purchasedValue.clear();
                   purchaseDate.setValue(null);
                   warrantyExpiration.setValue(null);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValid(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @FXML
    private void showHomePage(ActionEvent event) {
        try {
            // Load the new page FXML file
            root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root, 400, 400);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
