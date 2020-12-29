package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.WijzigHaaltAf;
import be.kuleuven.csa.jdbi.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class WijzigPakketKlantController {
    private String klantNaam;
    private int selectedRow;
    private String teWijzigenBoerNaam;
    private String teWijzigenPakketSoort;
    private int aangeduidPakketPrijs;

    public ChoiceBox<String> wijzigPakketKeuzeBoer_choice;
    public ChoiceBox<String> wijzigPakketKeuzePakket_choice;
    public Text wijzigPakketPrijsPakket_text;
    public Button wijzigPakket_button;

    private static KlantRepository klantRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;

    public void initialize() throws IOException {
        setUpRepo();

        //BUTTON actons
        wijzigPakket_button.setOnAction(e -> wijzigPakket());
    }

    //Choicebox instellen en prijs bepalen/weergeven
    public void refreshItems() {
        List<String> boerNamen = boerRepository.getAlleBoerNamen();
        List<String> pakketFormaten = Arrays.asList("Medium (2 volwassenen, 0 kinderen)", "Groot (2 volwassenen, 2 kinderen)", "Familie (2 volwassenen, 4 kinderen)");

        wijzigPakketKeuzeBoer_choice.setItems(FXCollections.observableArrayList(boerNamen));
        wijzigPakketKeuzePakket_choice.setItems(FXCollections.observableArrayList(pakketFormaten));

        wijzigPakketKeuzeBoer_choice.setValue(teWijzigenBoerNaam);
        wijzigPakketKeuzePakket_choice.setValue(teWijzigenPakketSoort);
        if (wijzigPakketPrijsPakket_text.getText().equals("....")){
            wijzigPakketPrijsPakket_text.setText(aangeduidPakketPrijs + " euro");
        }

        wijzigPakketKeuzeBoer_choice.setOnAction((event) -> {
            String selectedBoer = wijzigPakketKeuzeBoer_choice.getSelectionModel().getSelectedItem();
            String selectedPakket = wijzigPakketKeuzePakket_choice.getSelectionModel().getSelectedItem();
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
                wijzigPakketPrijsPakket_text.setText(prijs + " euro");
            }
        });
        wijzigPakketKeuzePakket_choice.setOnAction((event) -> {
            String selectedBoer = wijzigPakketKeuzeBoer_choice.getSelectionModel().getSelectedItem();
            String selectedPakket = wijzigPakketKeuzePakket_choice.getSelectionModel().getSelectedItem();
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
                wijzigPakketPrijsPakket_text.setText(prijs + " euro");
            }
        });
    }

    //Schrijft in aanpassen naar nieuw pakket (oude nog niet afgehaalde pakketten kunnen nog steeds afgehaald worden)
    public void wijzigPakket() {
        List<String> pakketFormaten = Arrays.asList("Medium (2 volwassenen, 0 kinderen)", "Groot (2 volwassenen, 2 kinderen)", "Familie (2 volwassenen, 4 kinderen)");

        String selectedBoer = wijzigPakketKeuzeBoer_choice.getSelectionModel().getSelectedItem();
        String selectedPakket = wijzigPakketKeuzePakket_choice.getSelectionModel().getSelectedItem();
        if (selectedBoer != null && selectedPakket != null) {
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

            List<Verkoopt> oudeVerkooptList = verkooptRepository.getVerkooptByKlantName(klantNaam);
            int oudeVerkoopt_id = oudeVerkooptList.get(selectedRow).getVerkoopt_id();

            List<Klant> klantList = klantRepository.getKlantByName(klantNaam);
            int klant_id = klantList.get(0).getAuteur_id();

            List<SchrijftIn> controleerSchrijftInLijst = verkooptRepository.getSchrijftInByKlantIDEnVerkooptID(klant_id, nieuwVerkoopt_id);
            if (controleerSchrijftInLijst.isEmpty()) {

                List<SchrijftIn> schrijftInList = verkooptRepository.getSchrijftInByKlantIDEnVerkooptID(klant_id, oudeVerkoopt_id);
                SchrijftIn schrijftIn = schrijftInList.get(0);
                schrijftIn.setVerkoopt_id(nieuwVerkoopt_id);
                verkooptRepository.wijzigSchrijftIn(schrijftIn);

                BestaandeKlantController.getInstance().refreshTable();
                BestaandeKlantController.getInstance().updateTeBetalenBedragVanKlanten();
                Stage stage = (Stage) wijzigPakket_button.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Warning!", "U bent al geabonneerd op dit pakket");
            }
        } else {
            showAlert("Warning!", "Gelieve beide velden in te vullen");
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
    }

    //Warning pop-up
    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Meegegeven data vorig scherm
    public void getNaamEnGeselecteerdPakket(String klantNaam, int selectedRow, String aangeduideBoerNaam, String aangeduidPakketNaam, int aangeduidPakketPrijs) {
        this.klantNaam = klantNaam;
        this.selectedRow = selectedRow;
        this.teWijzigenBoerNaam = aangeduideBoerNaam;
        switch (aangeduidPakketNaam) {
            case "Mediumpakket":
                this.teWijzigenPakketSoort = "Medium (2 volwassenen, 0 kinderen)";
                break;
            case "Grootpakket":
                this.teWijzigenPakketSoort = "Groot (2 volwassenen, 2 kinderen)";
                break;
            case "Familiepakket":
                this.teWijzigenPakketSoort = "Familie (2 volwassenen, 4 kinderen)";
                break;
        }
        this.aangeduidPakketPrijs = aangeduidPakketPrijs;
        refreshItems();
    }

}
