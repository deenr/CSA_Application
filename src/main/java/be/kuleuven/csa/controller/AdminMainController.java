package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.*;
import be.kuleuven.csa.jdbi.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.StringUtils;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Document;
import org.lightcouch.ReplicatorDocument;
import org.lightcouch.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public TableView<DataVoorTipTableView> tipsAdmin_table;
    public Button verwijderTipAdmin_button;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;
    private static ZitInRepository zitInRepository;
    private static ProductRepository productRepository;

    public void initialize() throws IOException {
        setUpRepo();

        //Button actions
        applyProductSoortAdmin_button.setOnAction(e -> refreshDataProducten());
        resetProductSoortAdmin_button.setOnAction(e -> resetFilterProductSoort());

        applyFilterPakketSoortAdmin_button.setOnAction(e -> refreshDataPakketten());
        resetFilterPakketSoortAdmin_button.setOnAction(e -> resetFilterPakketSoort());

        verwijderKlantAdmin_button.setOnAction(e -> verwijderKlant());
        verwijderBoerAdmin_button.setOnAction(e -> verwijderBoer());
        verwijderTipAdmin_button.setOnAction(e -> {
            try {
                verwijderTip();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        //Load data
        getDataKlanten();
        getDataBoeren();
        getDataProducten();
        getDataPakketten();
        getDataTips();

        //Refresh data
        refreshDataKlanten();
        refreshDataBoeren();
        refreshDataProducten();
        refreshDataPakketten();
        refreshDataTips();
    }

    private static void setUpRepo() throws IOException {
        var databaseFile = new String(Files.readAllBytes(Paths.get(MainDatabase.DatabasePath)));
        if (databaseFile.isEmpty()) {
            ConnectionManager connectionManager = new ConnectionManager();
            connectionManager.flushConnection();
        }
        var jdbi = Jdbi.create(ConnectionManager.ConnectionString);
        jdbi.installPlugin(new SqlObjectPlugin());

        //Repositories aanmaken
        auteurRepository = new AuteurRepositoryJdbi3Impl(jdbi);
        klantRepository = new KlantRepositoryJdbi3Impl(jdbi);
        pakketRepository = new PakketRepositoryJdbi3Impl(jdbi);
        boerRepository = new BoerRepositoryJdbi3Impl(jdbi);
        verkooptRepository = new VerkooptRepositoryJdbi3Impl(jdbi);
        zitInRepository = new ZitInRepositoryJdbi3Impl(jdbi);
        productRepository = new ProductRepositoryJdbi3Impl(jdbi);
    }

    // DATA KLANTEN:
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

    // Verwijder geselecteerde klant
    public void verwijderKlant() {
        DataVoorKlantTableView klant = klantenAdmin_table.getSelectionModel().getSelectedItem();
        if (klant == null) {
            showWarning("Warning", "Gelieve een klant te selecteren");
        } else {
            String klant_naam = klant.getAuteur_naam();
            int klant_id = klantRepository.getKlantByName(klant_naam).get(0).getAuteur_id();

            // In elke tabel waar deze klant_id staat rij verwijderen
            verkooptRepository.verwijderHaaltAfByAuteurID(klant_id);
            verkooptRepository.verwijderSchrijftInByAuteurID(klant_id);
            klantRepository.verwijderKlantByAuteurID(klant_id);
            auteurRepository.verwijderAuteurByAuteurID(klant_id);

            refreshDataKlanten();
        }
    }

    // DATA BOEREN:
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

    // Verwijder geselecteerde boer
    public void verwijderBoer() {
        //Selected item
        DataVoorBoerTableView boer = boerenAdmin_table.getSelectionModel().getSelectedItem();
        if (boer == null) {
            showWarning("Warning", "Gelieve een boer te selecteren");
        } else {
            String boer_naam = boer.getAuteur_naam();
            int boer_id = boerRepository.getBoerByName(boer_naam).get(0).getAuteur_id();
            List<Verkoopt> verkooptList = verkooptRepository.getVerkooptByBoerID(boer_id);

            // In elke tabel waar deze boer_id of zijn verkoopt_id staat rij verwijderen
            for (Verkoopt v : verkooptList) {
                zitInRepository.verwijderZitInByVerkooptID(v.getVerkoopt_id());
            }
            verkooptRepository.verwijderSchrijftInByAuteurID(boer_id);
            verkooptRepository.verwijderVerkooptByAuteurID(boer_id);
            boerRepository.verwijderKlantByAuteurID(boer_id);
            auteurRepository.verwijderAuteurByAuteurID(boer_id);

            refreshDataBoeren();
        }
    }

    // DATA PRODUCTEN:
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

    // DATA PAKKETTEN:
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

    // DATA KLANTEN:
    private void getDataTips() {
        tipsAdmin_table.getColumns().clear();
        tipsAdmin_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<DataVoorTipTableView, String> colAuteurNaam = new TableColumn<>("Auteur naam");
        colAuteurNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAuteur_naam()));
        tipsAdmin_table.getColumns().add(colAuteurNaam);
        TableColumn<DataVoorTipTableView, String> colProduct = new TableColumn<>("Product naam");
        colProduct.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getProduct_naam()));
        tipsAdmin_table.getColumns().add(colProduct);
        TableColumn<DataVoorTipTableView, String> colProductSoort = new TableColumn<>("Product soort");
        colProductSoort.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getProduct_soort()));
        tipsAdmin_table.getColumns().add(colProductSoort);
        TableColumn<DataVoorTipTableView, String> colURL = new TableColumn<>("Tip");
        colURL.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getTip_url()));
        tipsAdmin_table.getColumns().add(colURL);
    }

    private void refreshDataTips() {
        tipsAdmin_table.getItems().clear();

        CouchDbClient dbClient = new CouchDbClient();
        List<DataVoorTipTableView> dataVoorTipTableViews = new ArrayList<>();

        List<Tip> tipList = dbClient.view("_all_docs")
                .includeDocs(true)
                .query(Tip.class);
        for (Tip tip : tipList) {
            String auteur_naam = tip.getAuteur_naam();
            String tip_file = tip.getTip_file();

            int product_id = tip.getProduct_id();
            Product product = productRepository.getProductByProductID(product_id).get(0);
            String product_naam = product.getProduct_naam();
            String product_soort = product.getProduct_soort();

            dataVoorTipTableViews.add(new DataVoorTipTableView(auteur_naam, product_naam, product_soort, tip_file));
        }
        dbClient.shutdown();

        for (DataVoorTipTableView tip : dataVoorTipTableViews) {
            tipsAdmin_table.getItems().add(tip);
        }
    }

    // Verwijder geselecteerde klant
    public void verwijderTip() throws IOException {
        DataVoorTipTableView tipTableView = tipsAdmin_table.getSelectionModel().getSelectedItem();
        if (tipTableView == null) {
            showWarning("Warning", "Gelieve een tip te selecteren");
        } else {
            CouchDbClient dbClient = new CouchDbClient();
            String product_naam = tipTableView.getProduct_naam();
            int product_id = productRepository.getProductByName(product_naam).get(0).getProduct_id();
            String rev_id = getRevID(product_id, tipTableView.getTip_url());
            if (rev_id == null) {
                showWarning("Warning", "De tip is niet verwijderd, probeer het opnieuw");
            } else {
                Response response = dbClient.remove(product_id + tipTableView.getTip_url(), rev_id);
                if (response.getError() == null) {
                    dbClient.shutdown();
                } else {
                    showWarning("Warning", "De tip is niet verwijderd, probeer het opnieuw");
                }
            }
            dbClient.shutdown();
            refreshDataTips();
        }
    }

    // _rev_id zoeken van de tip in couchdb aan de hand van commandline en de output hiervan in een string plaatsen (https://www.youtube.com/watch?v=moeoyqpS4KI)
    private String getRevID(int product_id, String url) throws IOException {
        Process p = Runtime.getRuntime().exec("cmd /c curl -X GET http://127.0.0.1:5984/" + MainDatabase.CouchDB + "/_all_docs -u "+MainDatabase.CouchDBUsername+":"+MainDatabase.CouchDBPassword);
        String s;
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((s = stdInput.readLine()) != null) {
            if (s.contains("\"id\":\"" + product_id + url +"\"")) {
                return s.substring(s.indexOf(":{\"rev\":\"") + 9, s.indexOf("\"}}"));
            }
        }
        return null;
    }

    //Warning pop-up
    public void showWarning(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
