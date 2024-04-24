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
