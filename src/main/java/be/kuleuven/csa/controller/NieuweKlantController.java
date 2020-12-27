package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.Auteur;
import be.kuleuven.csa.domain.AuteurRepository;
import be.kuleuven.csa.domain.Klant;
import be.kuleuven.csa.domain.KlantRepository;
import be.kuleuven.csa.jdbi.AuteurRepositoryJdbi3Impl;
import be.kuleuven.csa.jdbi.ConnectionManager;
import be.kuleuven.csa.jdbi.KlantRepositoryJdbi3Impl;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class NieuweKlantController {
    public Button registreerNieuweKlant_button;
    public TextField insertNieuweKlant_naam;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;

    public void initialize() throws IOException {
        setUpRepo();
        registreerNieuweKlant_button.setOnAction(e -> registreerNieuweKlant(insertNieuweKlant_naam.getText()));
    }

    public void registreerNieuweKlant(String naam) {
        List<Auteur> auteurList = auteurRepository.getAllAuteurs();
        for (Auteur a: auteurList
        ) {
            if (a.getAuteur_naam().equals(naam)) {
                showAlert("Warning", "Deze naam is al in gebruik, gelieve een andere te kiezen");
                return;
            }
        }

        Auteur nieuweAuteur = new Auteur(naam);
        auteurRepository.saveNewAuteur(nieuweAuteur);

        auteurList = auteurRepository.getAllAuteurs();
        int ID = 0;
        for (Auteur a: auteurList
             ) {
            if (a.getAuteur_naam().equals(naam)) {
                ID = a.getAuteur_id();
            }
        }
        
        Klant nieuweKlant = new Klant(ID);

        klantRepository.saveNewKlant(nieuweKlant);

        Stage stage = (Stage) insertNieuweKlant_naam.getScene().getWindow();
        stage.close();
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

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
