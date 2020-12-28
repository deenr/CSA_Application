package be.kuleuven.csa.controller;

import be.kuleuven.csa.CSAMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CSAMainController {
    public Button ikBenEenKlant_button;
    public Button ikBenEenBoer_button;
    public Button ikBenEenAdmin_button;
    public Button bekijkKooptips_button;

    public void initialize() {
        ikBenEenKlant_button.setOnAction(e -> showScherm("klant_main"));
        ikBenEenBoer_button.setOnAction(e -> showScherm("boer_main"));
        ikBenEenAdmin_button.setOnAction(e -> showScherm("admin_main"));
    }

    private void showScherm(String id) {
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource(resourceName));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(id);
            stage.initOwner(CSAMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm " + resourceName + " niet vinden", e);
        }
    }
}
