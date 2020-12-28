package be.kuleuven.csa.controller;

import be.kuleuven.csa.CSAMain;
import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.jdbi.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class BestaandeBoerController {

    public String boerNaam;
    public Button nieuweWeekPakkettenToevoegenBoer_button;
    public Button klantenBekijkenBoer_button;
    public Button klantenStatusUpdatenBoer_button;

    public Button productToevoegenBoer_button;
    public ChoiceBox<String> nieuwProductSoortBoer_choice;
    public TextField nieuwProductNaamBoer_text;

    public Button pakketPrijsWijzingenBoer_button;
    public ChoiceBox<String> wijzigenPakketFormaatBoer_choice;
    public TextField wijzigenNieuwePrijsBoer_text;
    public Text wijzingenOudePrijsBoer_text;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;
    private static ProductRepository productRepository;

    public Text welkomTitel_boer;


    public void initialize() throws IOException {
        setUpRepo();

        productToevoegenBoer_button.setOnAction(e -> voegProductToe());
        pakketPrijsWijzingenBoer_button.setOnAction(e -> wijzigPakketVeldenJuist());
        klantenBekijkenBoer_button.setOnAction(e -> showSchermBekijktKlanten("boer_bekijkt_klanten"));
        klantenStatusUpdatenBoer_button.setOnAction(e -> showSchermVeranderPakketStatus("klanten_status_updaten_boer"));

        refreshItems();
    }

    public void refreshItems() {
        //inladen product soort array in choiceBox voor nieuw product
        List<String> productSoorten = Arrays.asList("Groeten", "Fruit", "Vlees", "Bloemen");
        nieuwProductSoortBoer_choice.setItems(FXCollections.observableArrayList(productSoorten));

        //inladen pakket formaat array in choiceBox voor verandering prijs
        List<String> pakketFormaten = Arrays.asList("Medium", "Groot", "Familie");
        wijzigenPakketFormaatBoer_choice.setItems(FXCollections.observableArrayList(pakketFormaten));
        wijzigenPakketFormaatBoer_choice.setOnAction((event) -> {
            String selectedFormaat = wijzigenPakketFormaatBoer_choice.getSelectionModel().getSelectedItem();
            if (selectedFormaat != null) {
                List<Boer> boerList = boerRepository.getBoerByName(boerNaam);
                int auteur_id = boerList.get(0).getAuteur_id();
                int pakket_id = 0;
                if (selectedFormaat.equals(pakketFormaten.get(0))) {
                    pakket_id = 1;
                } else if (selectedFormaat.equals(pakketFormaten.get(1))) {
                    pakket_id = 2;
                } else if (selectedFormaat.equals(pakketFormaten.get(2))) {
                    pakket_id = 3;
                }

                List<Verkoopt> verkooptList = verkooptRepository.getVerkooptByBoerAndPakket(auteur_id, pakket_id);

                int prijs = verkooptList.get(0).getVerkoopt_prijs();
                wijzingenOudePrijsBoer_text.setText(prijs + " euro");
            }
        });

    }

    public void voegProductToe() {
        List<String> productSoorten = Arrays.asList("Groeten", "Fruit", "Vlees", "Bloemen");
        String selectedSoort = nieuwProductSoortBoer_choice.getSelectionModel().getSelectedItem();

        if (nieuwProductNaamBoer_text.getText() == null || selectedSoort == null) {
            showWarning("Warning!", "Gelieve alle velden in te vullen bij product toevoeging");
        } else {
            List<Product> productList = productRepository.getProductByName(nieuwProductNaamBoer_text.getText());
            if (productList.isEmpty()) {
                Product nieuwProduct = new Product(nieuwProductNaamBoer_text.getText(), selectedSoort);
                productRepository.saveNewProduct(nieuwProduct);

                nieuwProductNaamBoer_text.setText("");
                nieuwProductSoortBoer_choice.setItems(FXCollections.observableArrayList(productSoorten));
                showInformation("Succes!", "Het product is succesvol toegevoegd");
            } else {
                showWarning("Warning!", "Dit product staat al in de lijst");
            }
        }
    }

    private void wijzigPakketVeldenJuist() {
        String selectedFormaat = wijzigenPakketFormaatBoer_choice.getSelectionModel().getSelectedItem();
        if(selectedFormaat == null || wijzigenNieuwePrijsBoer_text.getText().isEmpty()) {
            showWarning("Warning!", "Gelieve alle velden in te vullen bij pakket prijs wijziging");
        } else {
            try {
                int nieuwePrijs = Integer.parseInt(wijzigenNieuwePrijsBoer_text.getText());
                wijzigPrijsPakket(selectedFormaat, nieuwePrijs);
            } catch (NumberFormatException nfe) {
                showWarning("Warning!", "Gelieve getallen in te vullen bij de nieuwe prijs");
            }
        }
    }

    private void wijzigPrijsPakket(String selectedFormaat, int nieuwePrijs) {
        List<String> pakketFormaten = Arrays.asList("Medium", "Groot", "Familie");
        int pakket_id = 0;
        if (selectedFormaat.equals(pakketFormaten.get(0))) {
            pakket_id = 1;
        } else if (selectedFormaat.equals(pakketFormaten.get(1))) {
            pakket_id = 2;
        } else if (selectedFormaat.equals(pakketFormaten.get(2))) {
            pakket_id = 3;
        }
        List<Boer> boerList = boerRepository.getBoerByName(boerNaam);
        int auteur_id = boerList.get(0).getAuteur_id();

        List<Verkoopt> verkooptList = verkooptRepository.getVerkooptByBoerAndPakket(auteur_id, pakket_id);
        Verkoopt verkoopt = verkooptList.get(0);
        verkoopt.setVerkoopt_prijs(nieuwePrijs);
        verkooptRepository.wijzigVerkoopt(verkoopt);

        updateTeBetalenBedragVanKlanten(verkoopt);

        wijzigenPakketFormaatBoer_choice.setItems(FXCollections.observableArrayList(pakketFormaten));
        wijzingenOudePrijsBoer_text.setText("...");
        wijzigenNieuwePrijsBoer_text.setText("");
        showInformation("Succes!", "De prijs van het pakket is succesvol gewijzigd");
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
        productRepository = new ProductRepositoryJdbi3Impl(jdbi);
    }

    public void showWarning(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showInformation(String title, String content) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSchermBekijktKlanten(String id) {
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            Parent root = (AnchorPane) loader.load();

            BoerBekijktKlantenController boerBekijktKlantenController = loader.getController();
            boerBekijktKlantenController.getBoerNaam(boerNaam);

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

    private void showSchermVeranderPakketStatus(String id) {
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            Parent root = (AnchorPane) loader.load();

            KlantenStatusUpdatenBoerController klantenStatusUpdatenBoerController = loader.getController();
            klantenStatusUpdatenBoerController.getBoerNaam(boerNaam);

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

    public void getNaamVanBestaandeBoer(String naam) {
        this.boerNaam = naam;
        welkomTitel_boer.setText("Welkom " + boerNaam + "!");
        refreshItems();
    }

    public void updateTeBetalenBedragVanKlanten(Verkoopt verkoopt) {
        List<Klant> klantenDieDitPakketHebben = klantRepository.getKlantByVerkooptID(verkoopt.getVerkoopt_id());

        for (Klant k : klantenDieDitPakketHebben) {
            List<Auteur> auteurList = auteurRepository.getAuteurByID(k.getAuteur_id());
            String klantNaam = auteurList.get(0).getAuteur_naam();

            List<Integer> verkooptPrijsList = verkooptRepository.getVerkooptPrijzenByName(klantNaam);
            int nieuwTotaleTeBetalenBedrag = 0;
            for (Integer prijs : verkooptPrijsList) {
                nieuwTotaleTeBetalenBedrag = nieuwTotaleTeBetalenBedrag + prijs;
            }
            List<Klant> klantList = klantRepository.getKlantByName(klantNaam);
            Klant klant = klantList.get(0);

            klant.setKlant_teBetalenBedrag(nieuwTotaleTeBetalenBedrag);

            klantRepository.updateKlant(klant);
        }
    }
}
