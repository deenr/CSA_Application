package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.KlantenBoer;
import be.kuleuven.csa.jdbi.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NieuwWeekpakketBoerController {
    public ChoiceBox<String> mediumGroenten1_choice;
    public ChoiceBox<String> mediumGroenten2_choice;
    public ChoiceBox<String> mediumFruit_choice;
    public ChoiceBox<String> mediumVlees_choice;
    public ChoiceBox<String> mediumBloemen_choice;
    public ChoiceBox<String> grootGroenten1_choice;
    public ChoiceBox<String> grootGroenten2_choice;
    public ChoiceBox<String> grootFruit_choice;
    public ChoiceBox<String> grootVlees_choice;
    public ChoiceBox<String> grootBloemen_choice;
    public ChoiceBox<String> famGroenten1_choice;
    public ChoiceBox<String> famGroenten2_choice;
    public ChoiceBox<String> famFruit_choice;
    public ChoiceBox<String> famVlees_choice;
    public ChoiceBox<String> famBloemen_choice;

    public TextField mediumAantalGroenten1_text;
    public TextField mediumAantalGroenten2_text;
    public TextField mediumAantalFruit_text;
    public TextField mediumAantalVlees_text;
    public TextField mediumAantalBloemen_text;
    public TextField grootAantalGroenten1_text;
    public TextField grootAantalGroenten2_text;
    public TextField grootAantalFruit_text;
    public TextField grootAantalVlees_text;
    public TextField grootAantalBloemen_text;
    public TextField famAantalGroenten1_text;
    public TextField famAantalGroenten2_text;
    public TextField famAantalFruit_text;
    public TextField famAantalVlees_text;
    public TextField famAantalBloemen_text;

    public Button voegPakkettenVanDeWeekToe_button;

    public Text titelAanmakenPakketten_text;

    private String boerNaam;

    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;
    private static ZitInRepository zitInRepository;
    private static ProductRepository productRepository;

    public void initialize() throws IOException {
        setUpRepo();

        //BUTTON action
        voegPakkettenVanDeWeekToe_button.setOnAction(e->voegWeekPakkettenToe());
    }

    //Choicebox instellen en weeknummer bepalen
    public void refreshData() {
        int boer_id = boerRepository.getBoerByName(boerNaam).get(0).getAuteur_id();
        int verkoopt_id = verkooptRepository.getVerkooptByBoer(boer_id).get(0).getVerkoopt_id();
        List<ZitIn> zitInList = zitInRepository.getAlleZitInByVerkoopID(verkoopt_id);

        int hoogsteWeeknr = 0;
        for (ZitIn zI : zitInList) {
            if (hoogsteWeeknr < zI.getZitIn_weeknr()) {
                hoogsteWeeknr = zI.getZitIn_weeknr();
            }
        }
        hoogsteWeeknr = hoogsteWeeknr + 1;

        titelAanmakenPakketten_text.setText("Hier kan je nieuwe pakketten voor weeknummer " + hoogsteWeeknr + " aanmaken");

        List<String> groentenList = productRepository.getAlleProductenBySoort("groenten");
        mediumGroenten1_choice.setItems(FXCollections.observableArrayList(groentenList));
        mediumGroenten2_choice.setItems(FXCollections.observableArrayList(groentenList));
        grootGroenten1_choice.setItems(FXCollections.observableArrayList(groentenList));
        grootGroenten2_choice.setItems(FXCollections.observableArrayList(groentenList));
        famGroenten1_choice.setItems(FXCollections.observableArrayList(groentenList));
        famGroenten2_choice.setItems(FXCollections.observableArrayList(groentenList));

        List<String> fruitList = productRepository.getAlleProductenBySoort("fruit");
        mediumFruit_choice.setItems(FXCollections.observableArrayList(fruitList));
        grootFruit_choice.setItems(FXCollections.observableArrayList(fruitList));
        famFruit_choice.setItems(FXCollections.observableArrayList(fruitList));

        List<String> vleesList = productRepository.getAlleProductenBySoort("vlees");
        mediumVlees_choice.setItems(FXCollections.observableArrayList(vleesList));
        grootVlees_choice.setItems(FXCollections.observableArrayList(vleesList));
        famVlees_choice.setItems(FXCollections.observableArrayList(vleesList));

        List<String> bloemenList = productRepository.getAlleProductenBySoort("bloemen");
        mediumBloemen_choice.setItems(FXCollections.observableArrayList(bloemenList));
        grootBloemen_choice.setItems(FXCollections.observableArrayList(bloemenList));
        famBloemen_choice.setItems(FXCollections.observableArrayList(bloemenList));
    }

    // Controleer of alle velden zijn ingevulde en er geen text in aantal staat
    public void voegWeekPakkettenToe() {
        if (mediumGroenten1_choice.getSelectionModel().getSelectedItem() == null ||
                mediumGroenten2_choice.getSelectionModel().getSelectedItem() == null ||
                grootGroenten1_choice.getSelectionModel().getSelectedItem() == null ||
                grootGroenten2_choice.getSelectionModel().getSelectedItem() == null ||
                famGroenten1_choice.getSelectionModel().getSelectedItem() == null ||
                famGroenten2_choice.getSelectionModel().getSelectedItem() == null ||
                mediumFruit_choice.getSelectionModel().getSelectedItem() == null ||
                grootFruit_choice.getSelectionModel().getSelectedItem() == null ||
                famFruit_choice.getSelectionModel().getSelectedItem() == null ||
                mediumVlees_choice.getSelectionModel().getSelectedItem() == null ||
                grootVlees_choice.getSelectionModel().getSelectedItem() == null ||
                famVlees_choice.getSelectionModel().getSelectedItem() == null ||
                mediumBloemen_choice.getSelectionModel().getSelectedItem() == null ||
                grootBloemen_choice.getSelectionModel().getSelectedItem() == null ||
                famBloemen_choice.getSelectionModel().getSelectedItem() == null ||
                mediumAantalGroenten1_text.getText().isEmpty() ||
                mediumAantalGroenten2_text.getText().isEmpty() ||
                mediumAantalFruit_text.getText().isEmpty() ||
                mediumAantalVlees_text.getText().isEmpty() ||
                mediumAantalBloemen_text.getText().isEmpty() ||
                grootAantalGroenten1_text.getText().isEmpty() ||
                grootAantalGroenten2_text.getText().isEmpty() ||
                grootAantalFruit_text.getText().isEmpty() ||
                grootAantalVlees_text.getText().isEmpty() ||
                grootAantalBloemen_text.getText().isEmpty() ||
                famAantalGroenten1_text.getText().isEmpty() ||
                famAantalGroenten2_text.getText().isEmpty() ||
                famAantalFruit_text.getText().isEmpty() ||
                famAantalVlees_text.getText().isEmpty() ||
                famAantalBloemen_text.getText().isEmpty()
            ) {
            showAlert("Warning", "Gelieve alle velden in te vullen");
        } else {
            try {
                int mediumAantalGroenten1 = Integer.parseInt(mediumAantalGroenten1_text.getText());
                int mediumAantalGroenten2 = Integer.parseInt(mediumAantalGroenten2_text.getText());
                int mediumAantalFruit = Integer.parseInt(mediumAantalFruit_text.getText());
                int mediumAantalVlees = Integer.parseInt(mediumAantalVlees_text.getText());
                int mediumAantalBloemen = Integer.parseInt(mediumAantalBloemen_text.getText());
                int grootAantalGroenten1 = Integer.parseInt(grootAantalGroenten1_text.getText());
                int grootAantalGroenten2 = Integer.parseInt(grootAantalGroenten2_text.getText());
                int grootAantalFruit = Integer.parseInt(grootAantalFruit_text.getText());
                int grootAantalVlees = Integer.parseInt(grootAantalVlees_text.getText());
                int grootAantalBloemen = Integer.parseInt(grootAantalBloemen_text.getText());
                int famAantalGroenten1 = Integer.parseInt(famAantalGroenten1_text.getText());
                int famAantalGroenten2 = Integer.parseInt(famAantalGroenten2_text.getText());
                int famAantalFruit = Integer.parseInt(famAantalFruit_text.getText());
                int famAantalVlees = Integer.parseInt(famAantalVlees_text.getText());
                int famAantalBloemen = Integer.parseInt(famAantalBloemen_text.getText());

                int boer_id = boerRepository.getBoerByName(boerNaam).get(0).getAuteur_id();
                int pakketMediumID = 1;
                int verkooptMedium_id = verkooptRepository.getVerkooptByBoerAndPakket(boer_id, pakketMediumID).get(0).getVerkoopt_id();
                int pakketGrootID = 2;
                int verkooptGroot_id = verkooptRepository.getVerkooptByBoerAndPakket(boer_id, pakketGrootID).get(0).getVerkoopt_id();
                int pakketFamilieID = 3;
                int verkooptFamilie_id = verkooptRepository.getVerkooptByBoerAndPakket(boer_id, pakketFamilieID).get(0).getVerkoopt_id();

                List<ZitIn> zitInList = zitInRepository.getAlleZitInByVerkoopID(verkooptFamilie_id);
                int hoogsteWeeknr = 0;
                for (ZitIn zI : zitInList) {
                    if (hoogsteWeeknr < zI.getZitIn_weeknr()) {
                        hoogsteWeeknr = zI.getZitIn_weeknr();
                    }
                }
                int weeknr = hoogsteWeeknr + 1;

                //medium zitIn toevoegen
                ZitIn zitIn1 = new ZitIn(productRepository.getProductByName(mediumGroenten1_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptMedium_id, mediumAantalGroenten1, weeknr);
                zitInRepository.voegZitInToe(zitIn1);
                ZitIn zitIn2 = new ZitIn(productRepository.getProductByName(mediumGroenten2_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptMedium_id, mediumAantalGroenten2, weeknr);
                zitInRepository.voegZitInToe(zitIn2);
                ZitIn zitIn3 = new ZitIn(productRepository.getProductByName(mediumFruit_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptMedium_id, mediumAantalFruit, weeknr);
                zitInRepository.voegZitInToe(zitIn3);
                ZitIn zitIn4 = new ZitIn(productRepository.getProductByName(mediumVlees_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptMedium_id, mediumAantalVlees, weeknr);
                zitInRepository.voegZitInToe(zitIn4);
                ZitIn zitIn5 = new ZitIn(productRepository.getProductByName(mediumBloemen_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptMedium_id, mediumAantalBloemen, weeknr);
                zitInRepository.voegZitInToe(zitIn5);

                //groot zitIn toevoegen
                ZitIn zitIn6 = new ZitIn(productRepository.getProductByName(grootGroenten1_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptGroot_id, grootAantalGroenten1, weeknr);
                zitInRepository.voegZitInToe(zitIn6);
                ZitIn zitIn7 = new ZitIn(productRepository.getProductByName(grootGroenten2_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptGroot_id, grootAantalGroenten2, weeknr);
                zitInRepository.voegZitInToe(zitIn7);
                ZitIn zitIn8 = new ZitIn(productRepository.getProductByName(grootFruit_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptGroot_id, grootAantalFruit, weeknr);
                zitInRepository.voegZitInToe(zitIn8);
                ZitIn zitIn9 = new ZitIn(productRepository.getProductByName(grootVlees_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptGroot_id, grootAantalVlees, weeknr);
                zitInRepository.voegZitInToe(zitIn9);
                ZitIn zitIn10 = new ZitIn(productRepository.getProductByName(grootBloemen_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptGroot_id, grootAantalBloemen, weeknr);
                zitInRepository.voegZitInToe(zitIn10);

                //familie zitIn toevoegen
                ZitIn zitIn11 = new ZitIn(productRepository.getProductByName(famGroenten1_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptFamilie_id, famAantalGroenten1, weeknr);
                zitInRepository.voegZitInToe(zitIn11);
                ZitIn zitIn12 = new ZitIn(productRepository.getProductByName(famGroenten2_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptFamilie_id, famAantalGroenten2, weeknr);
                zitInRepository.voegZitInToe(zitIn12);
                ZitIn zitIn13 = new ZitIn(productRepository.getProductByName(famFruit_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptFamilie_id, famAantalFruit, weeknr);
                zitInRepository.voegZitInToe(zitIn13);
                ZitIn zitIn14 = new ZitIn(productRepository.getProductByName(famVlees_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptFamilie_id, famAantalVlees, weeknr);
                zitInRepository.voegZitInToe(zitIn14);
                ZitIn zitIn15 = new ZitIn(productRepository.getProductByName(famBloemen_choice.getSelectionModel().getSelectedItem()).get(0).getProduct_id(), verkooptFamilie_id, famAantalBloemen, weeknr);
                zitInRepository.voegZitInToe(zitIn15);

                List<Verkoopt> verkooptList = verkooptRepository.getVerkooptByBoer(boer_id);
                for (Verkoopt v : verkooptList) {
                    int verkooptID = v.getVerkoopt_id();
                    List<Klant> klantList = klantRepository.getKlantByVerkooptID(verkooptID);
                    for (Klant k : klantList) {
                        int klantID = k.getAuteur_id();
                        HaaltAf nieuwHaaltAf = new HaaltAf(klantID, verkooptID, weeknr, 0);
                        verkooptRepository.voegHaaltAfToe(nieuwHaaltAf);
                    }
                }

                Stage stage = (Stage) famBloemen_choice.getScene().getWindow();
                stage.close();

            } catch (NumberFormatException nfe) {
                showAlert("Warning", "Gelieve getallen in de vullen bij de hoeveelheid");
            }
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

        klantRepository = new KlantRepositoryJdbi3Impl(jdbi);
        pakketRepository = new PakketRepositoryJdbi3Impl(jdbi);
        boerRepository = new BoerRepositoryJdbi3Impl(jdbi);
        verkooptRepository = new VerkooptRepositoryJdbi3Impl(jdbi);
        zitInRepository = new ZitInRepositoryJdbi3Impl(jdbi);
        productRepository = new ProductRepositoryJdbi3Impl(jdbi);
    }

    // Warning pop-up
    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Mee gegeven boernaam vorig scherm
    public void getBoerNaam(String boerNaam) {
        this.boerNaam = boerNaam;
        refreshData();
    }
}
