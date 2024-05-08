package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class SearchAssetController implements Initializable {
    @FXML
    private TextField assetName;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private ChoiceBox<String> locationChoiceBox;
    @FXML
    private ComboBox<String> searchComboBox;
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
    @FXML
    private List<String> assets = new ArrayList<>();


    private Parent root;
    private Scene scene;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assetNameFromCSV("assets.csv");
        searchComboBox.getItems().addAll(assets);
        categoriesFromCSV("categories.csv");
        categoryChoiceBox.getItems().addAll(categories);
        locationsFromCSV("storages.csv");
        locationChoiceBox.getItems().addAll(locations);

        searchComboBox.setOnMouseClicked(event -> {
            if (assets.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No assets to choose from.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    private void setDetails(ActionEvent event) {
        String path = "assets.csv";
        String line = "";
        String asset = searchComboBox.getValue();

        assetName.clear();
        categoryChoiceBox.setValue(null);
        locationChoiceBox.setValue(null);
        description.clear();
        purchasedValue.clear();
        purchaseDate.setValue(null);
        warrantyExpiration.setValue(null);

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String name = "";
            String category = "";
            String location = "";
            String purchaseDateStr = "";
            String descript = "";
            String purchaseValue = "";
            String expiration = "";

            if (asset == null || asset.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No assets to search from.");
                alert.showAndWait();
            }
            while ((line = br.readLine()) != null){
                String[] details = line.split(",");
                if (details.length > 0 && details[0].equalsIgnoreCase(asset)) {
                    name = !details[0].isEmpty() ? details[0] : "";
                    category = details.length > 1 && !details[1].isEmpty() ? details[1] : "";
                    location = details.length > 2 && !details[2].isEmpty() ? details[2] : "";
                    purchaseDateStr = details.length > 3 && !details[3].isEmpty() ? details[3] : "";
                    descript = details.length > 4 && !details[4].isEmpty() ? details[4] : "";
                    purchaseValue = details.length > 5 && !details[5].isEmpty() ? details[5] : "";
                    expiration = details.length > 6 && !details[6].isEmpty() ? details[6] : "";
                    break;
                }
            }

            LocalDate parsedPurchaseDate = null;
            if (!purchaseDateStr.isEmpty() && !purchaseDateStr.equals("000")) {
                parsedPurchaseDate = LocalDate.parse(purchaseDateStr);
            }

            LocalDate parsedExpirationDate = null;
            if (!expiration.isEmpty() && !expiration.equals("000")) {
                parsedExpirationDate = LocalDate.parse(expiration);
            }

            assetName.setText(name);
            categoryChoiceBox.setValue(category);
            locationChoiceBox.setValue(location);
            description.setText(descript);
            purchaseDate.setValue(parsedPurchaseDate);
            purchasedValue.setText(purchaseValue);
            warrantyExpiration.setValue(parsedExpirationDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assetNameFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 1 && !details[0].isEmpty()) {
                    assets.add(details[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void saveEdit(ActionEvent event) {
        String newName = assetName.getText();
        String oldName = searchComboBox.getValue(); // Assumes that the combo box displays the current (old) asset name before editing
        String category = categoryChoiceBox.getValue();
        String location = locationChoiceBox.getValue();
        LocalDate purchaseDate = this.purchaseDate.getValue();
        String description = this.description.getText();
        String purchasedValue = this.purchasedValue.getText();
        LocalDate warrantyExpiration = this.warrantyExpiration.getValue();

        if (newName.isEmpty() || category == null || location == null) {
            showAlert("Error", "No edit to save!");
            return;
        }

        String purchaseDateString = purchaseDate == null ? "" : purchaseDate.toString();
        String warrantyExpirationString = warrantyExpiration == null ? "" : warrantyExpiration.toString();

        try {
            updateAssetInCSV(oldName, newName, category, location, purchaseDateString, description, purchasedValue, warrantyExpirationString);
            showAlert("Success", "Asset updated successfully!");
            refreshAssetList(); // Refresh assets list to include the new name changes
        } catch (IOException e) {
            showAlert("Error", "Failed to update the asset.");
            e.printStackTrace();
        }
    }


    private void updateAssetInCSV(String oldName, String newName, String category, String location, String purchaseDate, String description, String purchasedValue, String warrantyExpiration) throws IOException {
        List<String> fileContent = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("assets.csv"));
        String line;
        boolean assetFound = false;

        while ((line = br.readLine()) != null) {
            String[] details = line.split(",");
            if (details.length > 0 && details[0].equalsIgnoreCase(oldName)) {
                String updatedLine = String.join(",", newName, category, location, purchaseDate, description, purchasedValue, warrantyExpiration);
                fileContent.add(updatedLine);
                assetFound = true;
            } else {
                fileContent.add(line);
            }
        }
        br.close();

        if (assetFound) {
            FileWriter csvWriter = new FileWriter("assets.csv", false);
            for (String contentLine : fileContent) {
                csvWriter.write(contentLine + System.lineSeparator());
            }
            csvWriter.flush();
            csvWriter.close();
        } else {
            showAlert("Error", "Asset not found.");
        }
    }

    
    
    @FXML
    private void deleteAsset(ActionEvent event) {
        String assetToDelete = assetName.getText(); // Get the name of the asset to delete from the text field

        if (assetToDelete.isEmpty()) {
            showAlert("Error", "Asset name is empty! Please enter an asset name to delete.");
            return;
        }

        List<String> fileContent = new ArrayList<>();
        boolean assetFound = false;

        try {
            File file = new File("assets.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            // Read through each line in the CSV
            while ((line = br.readLine()) != null) {
                // Split the line based on commas, assuming CSV standard format
                String[] details = line.split(",");
                // Check if the first element, assuming it's the asset name, matches the asset to delete
                if (details.length > 0 && !details[0].equalsIgnoreCase(assetToDelete)) {
                    fileContent.add(line); // If it doesn't match, keep the line
                } else {
                    assetFound = true; // If it matches, do not add to fileContent, effectively deleting it
                }
            }
            br.close();

            // Only rewrite the CSV if the asset was actually found and removed
            if (assetFound) {
                FileWriter fw = new FileWriter(file, false);
                for (String outputLine : fileContent) {
                    fw.write(outputLine + System.lineSeparator()); // Write each line back to the file
                }
                fw.close();

                showAlert("Success", "Asset deleted successfully!");
                clearFormFields(); // Clear all form fields
                refreshAssetList(); // Refresh the list of assets in the ComboBox
            } else {
                showAlert("Error", "Asset not found. Please check the asset name and try again.");
            }
        } catch (IOException e) {
            showAlert("Error", "An error occurred while attempting to delete the asset.");
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFormFields() {
        assetName.clear(); // Clear the asset name from the text field
        categoryChoiceBox.setValue(null); // Clear the selected category
        locationChoiceBox.setValue(null); // Clear the selected location
        description.clear(); // Clear the description text area
        purchasedValue.clear(); // Clear the purchased value field
        purchaseDate.setValue(null); // Reset the purchase date picker
        warrantyExpiration.setValue(null); // Reset the warranty expiration date picker
    }

    
    private void refreshAssetList() throws IOException {
        assets.clear();
        assetNameFromCSV("assets.csv");
        searchComboBox.setItems(FXCollections.observableArrayList(assets));
        searchComboBox.setValue(null); // Optionally set to the new name if preferred
        clearFormFields(); // Clear the form fields after update
    }

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
}
