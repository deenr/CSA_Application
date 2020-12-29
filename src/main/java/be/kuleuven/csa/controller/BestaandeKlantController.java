package be.kuleuven.csa.controller;

import be.kuleuven.csa.CSAMain;
import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.jdbi.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BestaandeKlantController {
    public Button nieuwPakketBestellen_button;
    public Button wijzigPakket_button;
    public Button verwijderPakket_button;
    public Button afTeHalenPakketten_button;
    public Button tipToevoegenKlant_button;
    public TableView<DataVoorAbonnementenTableView> bestaandeKlantPakketten_Tbl;

    public String klantNaam;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static VerkooptRepository verkooptRepository;

    private static BestaandeKlantController instance;
    public Text teBetalenBedrag_text;

    public BestaandeKlantController() {
        instance = this;
    }

    public static BestaandeKlantController getInstance() {
        return instance;
    }

    public void initialize() throws IOException {
        setUpRepo();

        //BUTTON actions
        wijzigPakket_button.setOnAction(e -> isEenRijSelecteerd());
        nieuwPakketBestellen_button.setOnAction(e -> showSchermNieuwPakket("nieuw_pakket_klant"));
        verwijderPakket_button.setOnAction(e -> verwijderPakket());
        afTeHalenPakketten_button.setOnAction(e -> showSchermAfTeHalenPakketten("aftehalen_pakketten_klant"));
        tipToevoegenKlant_button.setOnAction(e->showSchermTipToevoegen("tip_toevoegen"));

        //TABEL:
        bestaandeKlantPakketten_Tbl.getColumns().clear();

        bestaandeKlantPakketten_Tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<DataVoorAbonnementenTableView, String> colNaam = new TableColumn<>("Soort Pakket");
        colNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_naam()));
        bestaandeKlantPakketten_Tbl.getColumns().add(colNaam);

        TableColumn<DataVoorAbonnementenTableView, String> colBoer = new TableColumn<>("Boer");
        colBoer.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAuteur_naam()));
        bestaandeKlantPakketten_Tbl.getColumns().add(colBoer);

        TableColumn<DataVoorAbonnementenTableView, Integer> colPrijs = new TableColumn<>("Prijs");
        colPrijs.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getVerkoopt_prijs()));
        bestaandeKlantPakketten_Tbl.getColumns().add(colPrijs);

        TableColumn<DataVoorAbonnementenTableView, Integer> colAantalVolwassenen = new TableColumn<>("Aantal Volwassenen");
        colAantalVolwassenen.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_aantalVolwassenen()));
        bestaandeKlantPakketten_Tbl.getColumns().add(colAantalVolwassenen);

        TableColumn<DataVoorAbonnementenTableView, Integer> colAantalKinderen = new TableColumn<>("Aantal Kinderen");
        colAantalKinderen.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_aantalKinderen()));
        bestaandeKlantPakketten_Tbl.getColumns().add(colAantalKinderen);

        refreshTable();
    }

    public void refreshTable() {
        bestaandeKlantPakketten_Tbl.getItems().clear();

        List<DataVoorAbonnementenTableView> pakketBoerVoorTableList = pakketRepository.getDataForAbonnementenTableViewByKlantName(klantNaam);
        for (DataVoorAbonnementenTableView p : pakketBoerVoorTableList) {
            bestaandeKlantPakketten_Tbl.getItems().add(p);
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
        pakketRepository = new PakketRepositoryJdbi3Impl(jdbi);
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

    //Uitschrijven van klant op geselecteerd pakket
    private void verwijderPakket() {
        System.out.println("verwijderknop");
        DataVoorAbonnementenTableView pakketBoerVoorTable = bestaandeKlantPakketten_Tbl.getSelectionModel().getSelectedItem();
        if (pakketBoerVoorTable != null) {
            int pakket_id = pakketBoerVoorTable.getPakket_id();
            String boer_naam = pakketBoerVoorTable.getAuteur_naam();

            List<Auteur> auteurList = auteurRepository.getAuteurByName(boer_naam);
            int boer_id = auteurList.get(0).getAuteur_id();

            List<Klant> klantList = klantRepository.getKlantByName(klantNaam);
            int klant_id = klantList.get(0).getAuteur_id();

            List<Verkoopt> verkooptList = verkooptRepository.getVerkooptByBoerAndPakket(boer_id, pakket_id);
            int verkoopt_id = verkooptList.get(0).getVerkoopt_id();

            List<SchrijftIn> schrijftInList = verkooptRepository.getSchrijftInByKlantEnVerkoopt(klant_id, verkoopt_id);
            SchrijftIn teVerwijderenSchrijftIn = null;
            for (SchrijftIn sI : schrijftInList) {
                if (sI.getAuteur_id() == klant_id && sI.getVerkoopt_id() == verkoopt_id) {
                    teVerwijderenSchrijftIn = sI;
                }
            }
            if (teVerwijderenSchrijftIn != null) {
                verkooptRepository.verwijderSchrijftIn(teVerwijderenSchrijftIn);
                System.out.println("Verwijderd uit database: " + teVerwijderenSchrijftIn.toString());

                updateTeBetalenBedragVanKlanten();
                refreshTable();
            } else {
                showAlert("Error!", "Er is iets fout gegaan, probeer het opnieuw");
            }

        } else {
            showAlert("Warning!", "Selecteer een pakket dat u wenst te wijzigen of te annuleren");
        }

    }

    //Mee gegeven naam uit vorig scherm
    public void getNaamVanBestaandeKlant(String naam) {
        this.klantNaam = naam;
        refreshTable();
        updateTeBetalenBedragVanKlanten();
    }

    //Nieuw scherm openen
    private void showSchermNieuwPakket(String id) {
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            Parent root = (AnchorPane) loader.load();

            NieuwPakketKlantController nieuwPakketKlantController = loader.getController();
            nieuwPakketKlantController.getKlantNaam(klantNaam);

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

    //Nieuw scherm openen
    private void showSchermAfTeHalenPakketten(String id) {
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            Parent root = (AnchorPane) loader.load();

            AfTeHalenPakkettenKlantController afTeHalenPakkettenKlantController = loader.getController();
            afTeHalenPakkettenKlantController.getKlantNaam(klantNaam);

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

    //Nieuw scherm openen
    private void showSchermWijzigPakket(String id, int selectedRow, String teWijzigenBoerNaam, String teWijzigenPakketSoort, int teWijzigenPakketPrijs) {
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            Parent root = (AnchorPane) loader.load();

            WijzigPakketKlantController wijzigPakketKlantController = loader.getController();
            wijzigPakketKlantController.getNaamEnGeselecteerdPakket(klantNaam, selectedRow, teWijzigenBoerNaam, teWijzigenPakketSoort, teWijzigenPakketPrijs);

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

    //Nieuw scherm openen
    private void showSchermTipToevoegen(String id) {
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            Parent root = (AnchorPane) loader.load();

            TipToevoegenController tipToevoegenController = loader.getController();
            tipToevoegenController.getAuteurNaam(klantNaam);

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

    //Controleer of er een rij is geselecteerd
    private void isEenRijSelecteerd() {
        if (bestaandeKlantPakketten_Tbl.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Warning!", "Selecteer een pakket dat u wenst te wijzigen of te annuleren");
            return;
        }
        int teWijzigenKolomNummer = bestaandeKlantPakketten_Tbl.getSelectionModel().getSelectedIndex();
        String teWijzigenBoerNaam = bestaandeKlantPakketten_Tbl.getSelectionModel().getSelectedItem().getAuteur_naam();
        String teWijzigenPakketSoort = bestaandeKlantPakketten_Tbl.getSelectionModel().getSelectedItem().getPakket_naam();
        int teWijzigenPakketPrijs = bestaandeKlantPakketten_Tbl.getSelectionModel().getSelectedItem().getVerkoopt_prijs();
        showSchermWijzigPakket("wijzig_pakket_klant", teWijzigenKolomNummer, teWijzigenBoerNaam, teWijzigenPakketSoort, teWijzigenPakketPrijs);
    }

    //Te betalen bedrag van de klant berekenen
    public void updateTeBetalenBedragVanKlanten() {
        List<Integer> verkooptPrijsList = verkooptRepository.getVerkooptPrijzenByName(klantNaam);
        int nieuwTotaleTeBetalenBedrag = 0;
        for (Integer prijs : verkooptPrijsList) {
            nieuwTotaleTeBetalenBedrag = nieuwTotaleTeBetalenBedrag + prijs;
        }
        List<Klant> klantList = klantRepository.getKlantByName(klantNaam);
        Klant klant = klantList.get(0);

        klant.setKlant_teBetalenBedrag(nieuwTotaleTeBetalenBedrag);

        klantRepository.updateKlant(klant);

        teBetalenBedrag_text.setText("Te betalen bedrag: "+ nieuwTotaleTeBetalenBedrag);
    }
}
