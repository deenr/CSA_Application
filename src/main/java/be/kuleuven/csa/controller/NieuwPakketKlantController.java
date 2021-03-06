package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.jdbi.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class NieuwPakketKlantController {
    private String klantNaam;

    public ChoiceBox<String> nieuwPakketKeuzeBoer_choice;
    public ChoiceBox<String> nieuwPakketKeuzePakket_choice;
    public Text nieuwPakketPrijsPakket_text;
    public Button nieuwPakket_button;

    private static KlantRepository klantRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;
    private static ZitInRepository zitInRepository;

    public void initialize() throws IOException {
        setUpRepo();

        //BUTTON action
        nieuwPakket_button.setOnAction(e -> nieuwPakket());

        refreshItems();
    }

    //Choicebox set text en bereken prijs
    public void refreshItems() {
        List<String> boerNamen = boerRepository.getAlleBoerNamen();
        List<String> pakketFormaten = Arrays.asList("Medium (2 volwassenen, 0 kinderen)", "Groot (2 volwassenen, 2 kinderen)", "Familie (2 volwassenen, 4 kinderen)");

        nieuwPakketKeuzeBoer_choice.setItems(FXCollections.observableArrayList(boerNamen));
        nieuwPakketKeuzePakket_choice.setItems(FXCollections.observableArrayList(pakketFormaten));

        nieuwPakketKeuzeBoer_choice.setOnAction((event) -> {
            String selectedBoer = nieuwPakketKeuzeBoer_choice.getSelectionModel().getSelectedItem();
            String selectedPakket = nieuwPakketKeuzePakket_choice.getSelectionModel().getSelectedItem();
            if (selectedBoer != null && selectedPakket != null) {
                List<Boer> boerList = boerRepository.getBoerByName(selectedBoer);
                int auteur_id = boerList.get(0).getAuteur_id();
                int pakket_id = 0;
                if (selectedPakket.equals(pakketFormaten.get(0))) {
                    pakket_id = 1;
                } else if (selectedPakket.equals(pakketFormaten.get(1))) {
                    pakket_id = 2;
                } else if (selectedPakket.equals(pakketFormaten.get(2))) {
                    pakket_id = 3;
                }

                List<Verkoopt> verkooptList = verkooptRepository.getVerkooptByBoerIDAndPakketID(auteur_id, pakket_id);

                int prijs = verkooptList.get(0).getVerkoopt_prijs();
                nieuwPakketPrijsPakket_text.setText(prijs + " euro");
            }
        });
        nieuwPakketKeuzePakket_choice.setOnAction((event) -> {
            String selectedBoer = nieuwPakketKeuzeBoer_choice.getSelectionModel().getSelectedItem();
            String selectedPakket = nieuwPakketKeuzePakket_choice.getSelectionModel().getSelectedItem();
            if (selectedBoer != null && selectedPakket != null) {
                List<Boer> boerList = boerRepository.getBoerByName(selectedBoer);
                int auteur_id = boerList.get(0).getAuteur_id();
                int pakket_id = 0;
                if (selectedPakket.equals(pakketFormaten.get(0))) {
                    pakket_id = 1;
                } else if (selectedPakket.equals(pakketFormaten.get(1))) {
                    pakket_id = 2;
                } else if (selectedPakket.equals(pakketFormaten.get(2))) {
                    pakket_id = 3;
                }

                List<Verkoopt> verkooptList = verkooptRepository.getVerkooptByBoerIDAndPakketID(auteur_id, pakket_id);

                int prijs = verkooptList.get(0).getVerkoopt_prijs();
                nieuwPakketPrijsPakket_text.setText(prijs + " euro");
            }
        });
    }

    //Klant schrijft zich in op nieuw pakket
    public void nieuwPakket() {
        List<String> pakketFormaten = Arrays.asList("Medium (2 volwassenen, 0 kinderen)", "Groot (2 volwassenen, 2 kinderen)", "Familie (2 volwassenen, 4 kinderen)");

        String selectedBoer = nieuwPakketKeuzeBoer_choice.getSelectionModel().getSelectedItem();
        String selectedPakket = nieuwPakketKeuzePakket_choice.getSelectionModel().getSelectedItem();

        if (selectedBoer == null || selectedPakket == null){
            showAlert("Warning!", "Gelieve alle velden aan te duiden");
        } else {
            List<Boer> boerList = boerRepository.getBoerByName(selectedBoer);
            int boer_id = boerList.get(0).getAuteur_id();
            int pakket_id = 0;
            if (selectedPakket.equals(pakketFormaten.get(0))) {
                pakket_id = 1;
            } else if (selectedPakket.equals(pakketFormaten.get(1))) {
                pakket_id = 2;
            } else if (selectedPakket.equals(pakketFormaten.get(2))) {
                pakket_id = 3;
            }

            List<Verkoopt> nieuweVerkooptList = verkooptRepository.getVerkooptByBoerIDAndPakketID(boer_id, pakket_id);
            int nieuwVerkoopt_id = nieuweVerkooptList.get(0).getVerkoopt_id();

            List<ZitIn> zitInList = zitInRepository.getAlleZitInByVerkoopID(nieuwVerkoopt_id);

            int hoogsteWeeknr = 0;
            for (ZitIn zI : zitInList) {
                if (hoogsteWeeknr < zI.getZitIn_weeknr()) {
                    hoogsteWeeknr = zI.getZitIn_weeknr();
                }
            }

            if (hoogsteWeeknr == 0){
                showAlert("Warning!", "Deze boer heeft nog geen pakketten");
                return;
            }

            List<Klant> klantList = klantRepository.getKlantByName(klantNaam);
            int klant_id = klantList.get(0).getAuteur_id();

            List<SchrijftIn> controleerSchrijftInList = verkooptRepository.getSchrijftInByKlantIDEnVerkooptID(klant_id, nieuwVerkoopt_id);
            if (controleerSchrijftInList.isEmpty()) {
                SchrijftIn schrijftIn = new SchrijftIn(klant_id, nieuwVerkoopt_id);
                verkooptRepository.voegSchrijftInToe(schrijftIn);

                updateTeBetalenBedragVanKlanten();
                BestaandeKlantController.getInstance().refreshTable();
                BestaandeKlantController.getInstance().updateTeBetalenBedragVanKlanten();
                Stage stage = (Stage) nieuwPakket_button.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Warning!", "U bent al geabonneerd op dit pakket");
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
        boerRepository = new BoerRepositoryJdbi3Impl(jdbi);
        verkooptRepository = new VerkooptRepositoryJdbi3Impl(jdbi);
        zitInRepository = new ZitInRepositoryJdbi3Impl(jdbi);
    }

    // Warning pop-up
    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Mee gegeven klantnaam vorig scherm
    public void getKlantNaam(String klantNaam) {
        this.klantNaam = klantNaam;
        refreshItems();
    }

    //Totaal te betalen bedrag van de klant aanpassen bij klant
    public void updateTeBetalenBedragVanKlanten() {
        List<Integer> verkooptPrijsList = verkooptRepository.getVerkooptPrijzenByName(klantNaam);
        int nieuwTotaleTeBetalenBedrag = 0;
        for (Integer prijs: verkooptPrijsList) {
            nieuwTotaleTeBetalenBedrag = nieuwTotaleTeBetalenBedrag + prijs;
        }
        List<Klant> klantList = klantRepository.getKlantByName(klantNaam);
        Klant klant = klantList.get(0);

        klant.setKlant_teBetalenBedrag(nieuwTotaleTeBetalenBedrag);

        klantRepository.updateKlant(klant);
    }

}
