package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

public class Asset {
    private SimpleStringProperty assetName;
    private SimpleStringProperty category;
    private SimpleStringProperty location;
    private SimpleStringProperty description;
    private SimpleObjectProperty<LocalDate> purchaseDate;
    private SimpleStringProperty purchasedValue;
    private SimpleObjectProperty<LocalDate> warrantyExpiration;

    public Asset(String assetName, String category, String location, String description, LocalDate purchaseDate, String purchasedValue, LocalDate warrantyExpiration) {
        this.assetName = new SimpleStringProperty(assetName);
        this.category = new SimpleStringProperty(category);
        this.location = new SimpleStringProperty(location);
        this.description = new SimpleStringProperty(description);
        this.purchaseDate = new SimpleObjectProperty<>(purchaseDate);
        this.purchasedValue = new SimpleStringProperty(purchasedValue);
        this.warrantyExpiration = new SimpleObjectProperty<>(warrantyExpiration);
    }

    // Getters and Setters
    public String getAssetName() {
        return assetName.get();
    }

    public void setAssetName(String assetName) {
        this.assetName.set(assetName);
    }

    public SimpleStringProperty assetNameProperty() {
        return assetName;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate.get();
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate.set(purchaseDate);
    }

    public SimpleObjectProperty<LocalDate> purchaseDateProperty() {
        return purchaseDate;
    }

    public String getPurchasedValue() {
        return purchasedValue.get();
    }

    public void setPurchasedValue(String purchasedValue) {
        this.purchasedValue.set(purchasedValue);
    }

    public SimpleStringProperty purchasedValueProperty() {
        return purchasedValue;
    }

    public LocalDate getWarrantyExpiration() {
        return warrantyExpiration.get();
    }

    public void setWarrantyExpiration(LocalDate warrantyExpiration) {
        this.warrantyExpiration.set(warrantyExpiration);
    }

    public SimpleObjectProperty<LocalDate> warrantyExpirationProperty() {
        return warrantyExpiration;
    }
}
