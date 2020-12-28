package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.jdbi.*;
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

public class NieuweBoerController {
    public Button registreerNieuweBoer_button;
    public TextField insertNieuweBoer_naam;
    public TextField insertNieuweBoer_adres;

    public TextField insertNieuweBoer_prijsM;
    public TextField insertNieuweBoer_prijsG;
    public TextField insertNieuweBoer_prijsF;

    private static AuteurRepository auteurRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;


    public void initialize() throws IOException {
        setUpRepo();
        registreerNieuweBoer_button.setOnAction(e -> alleVeldenIngevuld());
    }

    public void alleVeldenIngevuld() {

        if(insertNieuweBoer_naam.getText().isEmpty() || insertNieuweBoer_adres.getText().isEmpty() ||
                insertNieuweBoer_prijsM.getText().isEmpty() || insertNieuweBoer_prijsG.getText().isEmpty() ||
                insertNieuweBoer_prijsF.getText().isEmpty()
        ){
            showAlert("Warning","Gelieven alle velden in te vullen");
        }
        else{
            try {
                int prijsM = Integer.parseInt(insertNieuweBoer_prijsM.getText());
                int prijsG = Integer.parseInt(insertNieuweBoer_prijsG.getText());
                int prijsF = Integer.parseInt(insertNieuweBoer_prijsF.getText());
                registreerNieuweBoer(prijsM, prijsG, prijsF);
            } catch (NumberFormatException nfe){
                showAlert("Warning", "Gelieven getallen in te vullen bij de prijs");
            }
        }
    }

    public void registreerNieuweBoer(int prijsMedium, int prijsGroot, int prijsFamilie) {
        String naam = insertNieuweBoer_naam.getText();
        String adres = insertNieuweBoer_adres.getText();
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
        Boer nieuweBoer = new Boer(ID,adres);
        boerRepository.saveNewBoer(nieuweBoer);

        Verkoopt nieuwMediumVerkoopt = new Verkoopt(ID,1,prijsMedium);
        Verkoopt nieuwGrootVerkoopt = new Verkoopt(ID,2,prijsGroot);
        Verkoopt nieuwFamilieVerkoopt = new Verkoopt(ID,3,prijsFamilie);

        verkooptRepository.maakNieuweVerkooptAan(nieuwMediumVerkoopt);
        verkooptRepository.maakNieuweVerkooptAan(nieuwGrootVerkoopt);
        verkooptRepository.maakNieuweVerkooptAan(nieuwFamilieVerkoopt);

        Stage stage = (Stage) insertNieuweBoer_naam.getScene().getWindow();
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
        boerRepository = new BoerRepositoryJdbi3Impl(jdbi);
        verkooptRepository = new VerkooptRepositoryJdbi3Impl(jdbi);
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
