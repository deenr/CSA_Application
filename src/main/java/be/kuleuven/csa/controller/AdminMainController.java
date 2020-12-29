package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.*;
import be.kuleuven.csa.jdbi.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class AdminMainController {
    public TabPane admin_tab;

    public TableView<DataVoorKlantTableView> klantenAdmin_table;
    public Button verwijderKlantAdmin_button;

    public TableView<DataVoorBoerTableView> boerenAdmin_table;
    public Button verwijderBoerAdmin_button;

    public TableView<Product> productenAdmin_table;
    public ChoiceBox<String> filterProductSoortAdmin_choice;
    public Button applyProductSoortAdmin_button;
    public Button resetProductSoortAdmin_button;

    public TableView<DataVoorPakketTableView> pakkettenAdmin_table;
    public ChoiceBox<String> filterPakketSoortAdmin_choice;
    public Button applyFilterPakketSoortAdmin_button;
    public Button resetFilterPakketSoortAdmin_button;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;
    private static ZitInRepository zitInRepository;
    private static ProductRepository productRepository;

    public void initialize() throws IOException {
        setUpRepo();

        applyProductSoortAdmin_button.setOnAction(e -> refreshDataProducten());
        resetProductSoortAdmin_button.setOnAction(e -> resetFilterProductSoort());

        applyFilterPakketSoortAdmin_button.setOnAction(e -> refreshDataPakketten());
        resetFilterPakketSoortAdmin_button.setOnAction(e -> resetFilterPakketSoort());

        verwijderKlantAdmin_button.setOnAction(e->verwijderKlant());
        verwijderBoerAdmin_button.setOnAction(e->verwijderBoer());

        getDataKlanten();
        getDataBoeren();
        getDataProducten();
        getDataPakketten();

        refreshDataKlanten();
        refreshDataBoeren();
        refreshDataProducten();
        refreshDataPakketten();

        //refreshTable();
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
        zitInRepository = new ZitInRepositoryJdbi3Impl(jdbi);
        productRepository = new ProductRepositoryJdbi3Impl(jdbi);
    }

    public void verwijderKlant() {
        DataVoorKlantTableView klant = klantenAdmin_table.getSelectionModel().getSelectedItem();
        if (klant == null) {
            showWarning("Warning", "Gelieve een klant te selecteren");
        } else {
            String klant_naam = klant.getAuteur_naam();
            String klant_id = klantRepository.getKlantByName(klant_naam).get(0).getAuteur_id() + "";

            verkooptRepository.verwijderHaaltAfByAuteurID(klant_id);
            verkooptRepository.verwijderSchrijftInByAuteurID(klant_id);
            klantRepository.verwijderKlantByAuteurID(klant_id);
            auteurRepository.verwijderAuteurByAuteurID(klant_id);

            refreshDataKlanten();
        }

    }

    public void verwijderBoer() {

    }

    private void getDataKlanten() {
        klantenAdmin_table.getColumns().clear();
        klantenAdmin_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<DataVoorKlantTableView, String> colKlantNaam = new TableColumn<>("Naam");
        colKlantNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAuteur_naam()));
        klantenAdmin_table.getColumns().add(colKlantNaam);
        TableColumn<DataVoorKlantTableView, Integer> colKlantBedrag = new TableColumn<>("Te betalen bedrag");
        colKlantBedrag.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getKlant_teBetalenBedrag()));
        klantenAdmin_table.getColumns().add(colKlantBedrag);
    }

    private void refreshDataKlanten() {
        klantenAdmin_table.getItems().clear();
        List<DataVoorKlantTableView> dataVoorKlantTableViewList = klantRepository.getAlleKlantenVoorDataView();
        for (DataVoorKlantTableView dataKlant : dataVoorKlantTableViewList) {
            klantenAdmin_table.getItems().add(dataKlant);
        }
    }

    private void getDataBoeren() {
        boerenAdmin_table.getColumns().clear();
        boerenAdmin_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<DataVoorBoerTableView, String> colBoerNaam = new TableColumn<>("Boer naam");
        colBoerNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAuteur_naam()));
        boerenAdmin_table.getColumns().add(colBoerNaam);
        TableColumn<DataVoorBoerTableView, String> colBoerAdres = new TableColumn<>("Boer adres");
        colBoerAdres.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getBoer_adres()));
        boerenAdmin_table.getColumns().add(colBoerAdres);
    }

    private void refreshDataBoeren() {
        boerenAdmin_table.getItems().clear();
        List<DataVoorBoerTableView> dataVoorKlantTableViewList = boerRepository.getAlleBoerenVoorDataView();
        for (DataVoorBoerTableView dataBoer : dataVoorKlantTableViewList) {
            boerenAdmin_table.getItems().add(dataBoer);
        }
    }

    private void getDataProducten() {
        List<String> productSoorten = Arrays.asList("Alles", "Groenten", "Fruit", "Vlees", "Bloemen");
        filterProductSoortAdmin_choice.setItems(FXCollections.observableArrayList(productSoorten));
        if (filterProductSoortAdmin_choice.getSelectionModel().getSelectedItem() == null) {
            filterProductSoortAdmin_choice.setValue("Alles");
        }
        productenAdmin_table.getColumns().clear();
        productenAdmin_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Product, String> colProductNaam = new TableColumn<>("Product naam");
        colProductNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getProduct_naam()));
        productenAdmin_table.getColumns().add(colProductNaam);
        TableColumn<Product, String> colProductSoort = new TableColumn<>("Product soort");
        colProductSoort.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getProduct_soort()));
        productenAdmin_table.getColumns().add(colProductSoort);
    }

    private void refreshDataProducten() {
        productenAdmin_table.getItems().clear();
        List<Product> productList = productRepository.getAlleProductenVoorDataView();
        for (Product product : productList) {
            switch (filterProductSoortAdmin_choice.getSelectionModel().getSelectedItem()) {
                case "Alles":
                    productenAdmin_table.getItems().add(product);
                    break;
                case "Groenten":
                    if (product.getProduct_soort().equals("groenten")) {
                        productenAdmin_table.getItems().add(product);
                    }
                    break;
                case "Fruit":
                    if (product.getProduct_soort().equals("fruit")) {
                        productenAdmin_table.getItems().add(product);
                    }
                    break;
                case "Vlees":
                    if (product.getProduct_soort().equals("vlees")) {
                        productenAdmin_table.getItems().add(product);
                    }
                    break;
                case "Bloemen":
                    if (product.getProduct_soort().equals("bloemen")) {
                        productenAdmin_table.getItems().add(product);
                    }
                    break;
            }
        }
    }

    private void resetFilterProductSoort() {
        filterProductSoortAdmin_choice.setValue("Alles");
        refreshDataProducten();
    }

    private void getDataPakketten() {
        List<String> pakketSoort = Arrays.asList("Alles", "Medium", "Groot", "Familie");
        filterPakketSoortAdmin_choice.setItems(FXCollections.observableArrayList(pakketSoort));
        if (filterPakketSoortAdmin_choice.getSelectionModel().getSelectedItem() == null) {
            filterPakketSoortAdmin_choice.setValue("Alles");
        }
        pakkettenAdmin_table.getColumns().clear();
        pakkettenAdmin_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<DataVoorPakketTableView, String> colPakketBoerNaam = new TableColumn<>("Boer naam");
        colPakketBoerNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getBoer_naam()));
        pakkettenAdmin_table.getColumns().add(colPakketBoerNaam);
        TableColumn<DataVoorPakketTableView, String> colPakketNaam = new TableColumn<>("Pakket naam");
        colPakketNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_naam()));
        pakkettenAdmin_table.getColumns().add(colPakketNaam);
        TableColumn<DataVoorPakketTableView, String> colPakketKlantNaam = new TableColumn<>("Klant naam");
        colPakketKlantNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getKlant_naam()));
        pakkettenAdmin_table.getColumns().add(colPakketKlantNaam);
        TableColumn<DataVoorPakketTableView, Integer> colPakketWeeknr = new TableColumn<>("Weeknummer");
        colPakketWeeknr.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getWeeknr()));
        pakkettenAdmin_table.getColumns().add(colPakketWeeknr);
        TableColumn<DataVoorPakketTableView, Integer> colPakketAfgehaald = new TableColumn<>("Afgehaald");
        colPakketAfgehaald.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_afgehaald()));
        pakkettenAdmin_table.getColumns().add(colPakketAfgehaald);
    }

    private void refreshDataPakketten() {
        pakkettenAdmin_table.getItems().clear();
        List<DataVoorPakketTableView> dataVoorKlantTableViewList = pakketRepository.getAllePakkettenVoorDataView();
        for (DataVoorPakketTableView dataVoorPakketTableView : dataVoorKlantTableViewList) {
            switch (filterPakketSoortAdmin_choice.getSelectionModel().getSelectedItem()) {
                case "Alles":
                    pakkettenAdmin_table.getItems().add(dataVoorPakketTableView);
                    break;
                case "Medium":
                    if (dataVoorPakketTableView.getPakket_naam().equals("Mediumpakket")) {
                        pakkettenAdmin_table.getItems().add(dataVoorPakketTableView);
                    }
                    break;
                case "Groot":
                    if (dataVoorPakketTableView.getPakket_naam().equals("Grootpakket")) {
                        pakkettenAdmin_table.getItems().add(dataVoorPakketTableView);
                    }
                    break;
                case "Familie":
                    if (dataVoorPakketTableView.getPakket_naam().equals("Familiepakket")) {
                        pakkettenAdmin_table.getItems().add(dataVoorPakketTableView);
                    }
                    break;
            }
        }
    }

    private void resetFilterPakketSoort() {
        filterPakketSoortAdmin_choice.setValue("Alles");
        refreshDataPakketten();
    }

    public void showWarning(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showError(String title, String content) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
