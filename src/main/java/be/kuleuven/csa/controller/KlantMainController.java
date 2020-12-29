package be.kuleuven.csa.controller;

import be.kuleuven.csa.CSAMain;
import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.Auteur;
import be.kuleuven.csa.domain.AuteurRepository;
import be.kuleuven.csa.domain.Klant;
import be.kuleuven.csa.domain.KlantRepository;
import be.kuleuven.csa.jdbi.AuteurRepositoryJdbi3Impl;
import be.kuleuven.csa.jdbi.ConnectionManager;
import be.kuleuven.csa.jdbi.KlantRepositoryJdbi3Impl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class KlantMainController {
    public Button nieuweKlant_button;

    public Button bestaandeKlantLogin_button;
    public TextField bestaandeKlant_naam;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;

    public void initialize() throws IOException {
        setUpRepo();

        //BUTTON action
        nieuweKlant_button.setOnAction(e -> showScherm("nieuwe_klant"));
        bestaandeKlantLogin_button.setOnAction(e -> controleerNaamInDatabase("bestaande_klant"));
    }

    //Nieuw scherm aanmaken
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

    // Controleert of klant bestaat in database (voor login)
    public void controleerNaamInDatabase(String id) {
        List<Auteur> auteurList = auteurRepository.getAllAuteurs();
        boolean naamBestaand = false;
        String naam = "";
        for (Auteur a : auteurList
        ) {
            if (a.getAuteur_naam().equals(bestaandeKlant_naam.getText())) {
                naamBestaand = true;
                naam = a.getAuteur_naam();
            }
        }
        if (naamBestaand) {
            List<Klant> klantList = klantRepository.getKlantByName(naam);
            if (klantList.isEmpty()) {
                showError("Error", "De ingegeven naam is niet geregistreerd als klant");
            } else {
                showSchermMetData(id);
            }
        } else {
            showWarning("Warning", "Deze naam is niet in gebruik, gelieve u te registreren");
        }
    }

    // Nieuw scherm aanmaken
    private void showSchermMetData(String id) {
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            Parent root = (AnchorPane) loader.load();

            BestaandeKlantController bestaandeKlantController = loader.getController();
            bestaandeKlantController.getNaamVanBestaandeKlant(bestaandeKlant_naam.getText());

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

    private static void setUpRepo() throws IOException {
        var databaseFile = new String(Files.readAllBytes(Paths.get(MainDatabase.DatabasePath)));
        if (databaseFile.isEmpty()) {
            ConnectionManager connectionManager = new ConnectionManager();
            connectionManager.flushConnection();
        }
        var jdbi = Jdbi.create(ConnectionManager.ConnectionString);
        jdbi.installPlugin(new SqlObjectPlugin());

        auteurRepository = new AuteurRepositoryJdbi3Impl(jdbi);
        klantRepository = new KlantRepositoryJdbi3Impl(jdbi);
    }

    //Warning pop-up
    public void showWarning(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Error pop-up
    public void showError(String title, String content) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
