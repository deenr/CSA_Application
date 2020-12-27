package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.jdbi.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.text.Text;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class WijzigPakketKlantController {
    private String klantNaam;

    public ChoiceBox<String> wijzigPakketKeuzeBoer_choice;
    public ChoiceBox<String> wijzigPakketKeuzePakket_choice;
    public Text wijzigPakketPrijsPakket_text;
    public Button wijzigPakket_button;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;

    public void initialize() throws IOException {
        setUpRepo();
        refreshItems();

    }

    public void refreshItems() {
        List<String> boerNamen = boerRepository.getAlleBoerNamen();
        List<String> pakketFormaten = Arrays.asList("Medium","Groot", "Familie");

        wijzigPakketKeuzeBoer_choice.setItems(FXCollections.observableArrayList(boerNamen));
        wijzigPakketKeuzePakket_choice.setItems(FXCollections.observableArrayList(pakketFormaten));

        wijzigPakketKeuzeBoer_choice.setOnAction((event) -> {
            Object selectedItem = wijzigPakketKeuzeBoer_choice.getSelectionModel().getSelectedItem();
        });

        wijzigPakketKeuzePakket_choice.setOnAction((event) -> {
            Object selectedItem = wijzigPakketKeuzeBoer_choice.getSelectionModel().getSelectedItem();
        });

        int pakket_id = 0;
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
        pakketRepository = new PakketRepositoryJdbi3Impl(jdbi);
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

    public void getNaamVanKlant(String klantNaam) {
        this.klantNaam = klantNaam;
        refreshItems();
    }
}
