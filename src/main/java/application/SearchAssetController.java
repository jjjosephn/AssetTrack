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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class SearchAssetController implements Initializable {
	
	@FXML
    private TableView<Asset> table;
    @FXML
    private TableColumn<Asset, String> assets_col;
    @FXML
    private TableColumn<Asset, String> cat_col;
    @FXML
    private TableColumn<Asset, String> loc_col;
    @FXML
    private TableColumn<Asset, String> des_col;
    @FXML
    private TableColumn<Asset, LocalDate> pDate_col;
    @FXML
    private TableColumn<Asset, String> pVal_col;
    @FXML
    private TableColumn<Asset, LocalDate> we_col;

    private ObservableList<Asset> assetList = FXCollections.observableArrayList();

    @FXML
    private TextField searchAsset;
    @FXML
    private ChoiceBox<String> categorySearch;
    @FXML
    private ChoiceBox<String> locationSearch;


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
    private TextArea description;
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
        loadAssetsFromCSV("assets.csv");
        assetNameFromCSV("assets.csv");
        categoriesFromCSV("categories.csv");
        categoryChoiceBox.getItems().addAll(categories);
        categorySearch.getItems().addAll(categories);
        locationsFromCSV("storages.csv");
        locationChoiceBox.getItems().addAll(locations);
        locationSearch.getItems().addAll(locations);

        assets_col.setCellValueFactory(cellData -> cellData.getValue().assetNameProperty());
        cat_col.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        loc_col.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        des_col.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        pDate_col.setCellValueFactory(cellData -> cellData.getValue().purchaseDateProperty());
        pVal_col.setCellValueFactory(cellData -> cellData.getValue().purchasedValueProperty());
        we_col.setCellValueFactory(cellData -> cellData.getValue().warrantyExpirationProperty());

        table.setItems(assetList);

        //choosing asset by clicking on the table
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                setDetails(null);
            }
        });

        //choosing a category on the search menu
        categorySearch.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateTableViewForCategory(newValue);
                categorySearch.setValue(newValue);
            }
            locationSearch.setValue(null);
            searchAsset.setText(null);
        });

        //choosing a location on the search menu
        locationSearch.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateTableViewForLocation(newValue);
                locationSearch.setValue(newValue);
            }
            categorySearch.setValue(null);
            searchAsset.setText(null);
        });
    }

    //updating table after choosing a category from the search menu
    private void updateTableViewForCategory(String selectedCategory) {
        FilteredList<Asset> filteredList = assetList.filtered(asset ->
                asset.getCategory().equalsIgnoreCase(selectedCategory)
        );

        table.setItems(filteredList);
    }

    //updating table after choosing a location from the search menu
    private void updateTableViewForLocation(String selectedLocation) {
        FilteredList<Asset> filteredList = assetList.filtered(asset ->
                asset.getLocation().equalsIgnoreCase(selectedLocation)
        );

        table.setItems(filteredList);
        locationSearch.setValue(selectedLocation);
    }

    private void loadAssetsFromCSV(String filePath) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 7) {
                    LocalDate purchaseDate = null, warrantyExpiration = null;
                    try {
                        purchaseDate = LocalDate.parse(details[3], formatter); // Corrected index for purchase date
                        warrantyExpiration = LocalDate.parse(details[6], formatter); // Corrected index for warranty expiration date
                    } catch (DateTimeParseException e) {
                        System.err.println("Error parsing dates in line: " + line);
                        continue;
                    }
                    Asset asset = new Asset(
                            details[0],    // assetName
                            details[1],    // category
                            details[2],    // location
                            details[4],    // description
                            purchaseDate,
                            details[5],    // purchasedValue
                            warrantyExpiration);
                    assetList.add(asset);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded " + assetList.size() + " assets");
    }

    //Setting table info
    @FXML
    private void setDetails(ActionEvent event) {
        Asset selectedAsset = table.getSelectionModel().getSelectedItem();
        if (selectedAsset != null) {
            String assetNameStr = selectedAsset.getAssetName();
            String categoryStr = selectedAsset.getCategory();
            String locationStr = selectedAsset.getLocation();
            String purchaseDateStr = selectedAsset.getPurchaseDate().toString();
            String descriptionStr = selectedAsset.getDescription();
            String purchasedValueStr = selectedAsset.getPurchasedValue();
            String warrantyExpirationStr = selectedAsset.getWarrantyExpiration().toString();

            searchAsset.setText(assetNameStr);
            assetName.setText(assetNameStr);
            categoryChoiceBox.setValue(categoryStr);
            locationChoiceBox.setValue(locationStr);
            description.setText(descriptionStr);
            purchasedValue.setText(purchasedValueStr);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(purchaseDateStr, formatter);
            LocalDate warrantyDate = LocalDate.parse(warrantyExpirationStr, formatter);

            purchaseDate.setValue(date);
            warrantyExpiration.setValue(warrantyDate);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an asset from the table.");
            alert.showAndWait();
        }
    }

    //clicking the search buutton
    @FXML
    private void searchAsset(ActionEvent event) {
        if(searchAsset.getText() == null){
            showAlert("Error", "No assets to search for!");
        }
        else {
            String searchTerm = searchAsset.getText().toLowerCase().trim();
            // Create a filtered list based on the search term
            FilteredList<Asset> filteredList = assetList.filtered(asset ->
                    asset.getAssetName().toLowerCase().contains(searchTerm)
            );

            // Update the table view with the filtered list
            table.setItems(filteredList);
            categorySearch.setValue(null);
            locationSearch.setValue(null);
        }
    }

    @FXML
    private void refreshAsset(ActionEvent event){
        assetList.clear(); // Clear the current list of assets
        loadAssetsFromCSV("assets.csv"); // Reload assets from CSV
        table.setItems(assetList); // Update the TableView with the updated list of assets
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
//
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

    //save edit button
    @FXML
    private void saveEdit(ActionEvent event) {
        String newName = assetName.getText();
        String oldName = searchAsset.getText(); // Assumes that the search asset text field displays the current (old) asset name before editing
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

    //delete asset button
    @FXML
    private void deleteAsset(ActionEvent event) {
        String assetToDelete = assetName.getText(); // Get the name of the asset to delete from the text field
        searchAsset.setText(null);

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
                    fw.write(outputLine + System.lineSeparator());
                }
                fw.close();

                showAlert("Success", "Asset deleted successfully!");
                clearFormFields();
                refreshAssetList();
            } else {
                showAlert("Error", "Asset not found. Please choose an asset and try again.");
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
        assetName.clear();
        categoryChoiceBox.setValue(null);
        locationChoiceBox.setValue(null);
        description.clear();
        purchasedValue.clear();
        purchaseDate.setValue(null);
        warrantyExpiration.setValue(null);
    }


    private void refreshAssetList() throws IOException {
        assetList.clear();
        loadAssetsFromCSV("assets.csv");
        table.setItems(assetList);
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
