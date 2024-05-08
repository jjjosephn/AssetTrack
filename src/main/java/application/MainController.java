package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class MainController {
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	public void showDefineNewCategoryPage(ActionEvent event) {
		try {
			// Load the new page FXML file
			root = FXMLLoader.load(getClass().getResource("DefineNewCategory.fxml"));
			Scene scene = new Scene(root);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setTitle("Define New Category");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@FXML
	public void showDefineNewStoragePage(ActionEvent event) {
		try {
			// Load the new page FXML file
			root = FXMLLoader.load(getClass().getResource("DefineNewStorage.fxml"));
			Scene scene = new Scene(root);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setTitle("Define New Storage");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
    public void showDefineNewAssetPage(ActionEvent event) {
		try {
			// Load the new page FXML file
			root = FXMLLoader.load(getClass().getResource("DefineNewAsset.fxml"));
			Scene scene = new Scene(root);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setTitle("Define New Storage");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@FXML
	public void showSearchAssetPage(ActionEvent event) {
		try {
			// Load the new page FXML file
			root = FXMLLoader.load(getClass().getResource("SearchAsset.fxml"));
			Scene scene = new Scene(root);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setTitle("Search Asset");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void showExpiredWarrantiesPage(ActionEvent event) {
		try {
			// Load the new page FXML file
			root = FXMLLoader.load(getClass().getResource("ExpiredWarranties.fxml"));
			Scene scene = new Scene(root);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setTitle("Search Asset");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

}
