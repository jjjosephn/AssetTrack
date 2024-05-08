package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpiredWarrantiesController {
    @FXML
    private TableView<ExpiredAsset> tableView;
    @FXML
    private TableColumn<ExpiredAsset, String> expiredAssets;
    @FXML
    private TableColumn<ExpiredAsset, String> warrantyExpirationDates;

    @FXML
    private TextField assetBox;
    @FXML
    private TextField categoryBox;
    @FXML
    private TextField locationBox;
    @FXML
    private TextArea descriptionBox;
    @FXML
    private TextField purchaseDateBox;
    @FXML
    private TextField purchaseValueBox;
    @FXML
    private TextField warrantyExpirationDateBox;


    private Parent root;
    private Stage stage;

    @FXML
    private void initialize() {
        expiredAssets.setCellValueFactory(new PropertyValueFactory<>("asset"));
        warrantyExpirationDates.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));

        loadDataFromCSV();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection.getAsset());
            }
        });
    }

    public boolean hasExpiredWarranties(){
        if (!tableView.getItems().isEmpty()){
            return true;
        }
        return false;
    }

    public void showExpiredWarrantiesAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Expired Warranties");
        alert.setHeaderText("Expired Warranties Detected");
        alert.setContentText("Some of your assets have expired warranties");

        ButtonType okButton = new ButtonType("Ok");
        ButtonType showButton = new ButtonType("Show Me");

        alert.getButtonTypes().setAll(okButton, showButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == okButton) {
                loadHomePage();
            }
            if (buttonType == showButton) {
                loadExpiredWarrantiesPage();
            }
        });
    }

    private void populateFields(String assetName) {
        String path = "assets.csv";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length > 0 && details[0].equalsIgnoreCase(assetName)) {
                    assetBox.setText(details[0]);
                    categoryBox.setText(details[1]);
                    locationBox.setText(details[2]);
                    purchaseDateBox.setText(details[3]);
                    descriptionBox.setText(details[4]);
                    purchaseValueBox.setText(details[5]);
                    warrantyExpirationDateBox.setText(details[6]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromCSV() {
        List<ExpiredAsset> expiredAssetsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(new File("assets.csv")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String asset = parts[0];

                if (parts.length >= 7 && !parts[6].isEmpty()) {
                    LocalDate expirationDate = LocalDate.parse(parts[6]);

                    if (expirationDate.isBefore(LocalDate.now())) {
                        expiredAssetsList.add(new ExpiredAsset(asset, expirationDate.toString()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<ExpiredAsset> data = FXCollections.observableArrayList(expiredAssetsList);
        tableView.setItems(data);
    }

    private void loadExpiredWarrantiesPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ExpiredWarranties.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Expired Warranties Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //For after clicking Ok on Popup
    private void loadHomePage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Expired Warranties Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //For when clicking home button
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
