package be.kuleuven.csa.controller;

import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorAfhalingenTableView;
import be.kuleuven.csa.domain.helpdomain.Tip;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TipsMainController {
    public List<Tip> tipList;

    public TableView<Tip> tips_table;
    public ChoiceBox<String> filterProductSoort_choice;
    public Button applyFilterProductSoort_button;
    public Button resetFilterProductSoort_button;

    public void initialize() {
        loadDb();

        //BUTTON action
        applyFilterProductSoort_button.setOnAction(e -> applyFilterProductSoort());
        resetFilterProductSoort_button.setOnAction(e -> resetFilterProductSoort());

        tips_table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && tips_table.getSelectionModel().getSelectedItem() != null) {
                String websiteURL = tips_table.getSelectionModel().getSelectedItem().getTip_url();
                openWebsiteMetDezeURL(websiteURL);
            }
        });

        //TABEL
        tips_table.getColumns().clear();
        tips_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Tip, String> colAuteurNaam = new TableColumn<>("Auteur naam");
        colAuteurNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAuteur_naam()));
        tips_table.getColumns().add(colAuteurNaam);

        TableColumn<Tip, String> colProduct = new TableColumn<>("Product naam");
        colProduct.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getProduct_naam()));
        tips_table.getColumns().add(colProduct);

        TableColumn<Tip, String> colProductSoort = new TableColumn<>("Product soort");
        colProductSoort.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getProduct_soort()));
        tips_table.getColumns().add(colProductSoort);

        TableColumn<Tip, String> colURL = new TableColumn<>("Tip");
        colURL.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getTip_url()));
        tips_table.getColumns().add(colURL);
    }

    //Filter
    private void applyFilterProductSoort() {
        tips_table.getItems().clear();
        for (Tip tip : tipList) {
            if (filterProductSoort_choice.getSelectionModel().getSelectedItem().equals("Alles")) {
                tips_table.getItems().add(tip);
            } else if (filterProductSoort_choice.getSelectionModel().getSelectedItem().equals("Groenten")) {
                if (tip.getProduct_soort().equals("Groenten")) {
                    tips_table.getItems().add(tip);
                }
            } else if (filterProductSoort_choice.getSelectionModel().getSelectedItem().equals("Fruit")) {
                if (tip.getProduct_soort().equals("Fruit")) {
                    tips_table.getItems().add(tip);
                }
            } else if (filterProductSoort_choice.getSelectionModel().getSelectedItem().equals("Vlees")) {
                if (tip.getProduct_soort().equals("Vlees")) {
                    tips_table.getItems().add(tip);
                }
            }
        }
    }

    private void resetFilterProductSoort() {
        filterProductSoort_choice.setValue("Alles");
        insertInToTable();
    }

    //Toevoeging tabel items afhankelijk van filter
    private void insertInToTable() {
        tips_table.getItems().clear();

        List<String> productSoorten = Arrays.asList("Alles", "Groenten", "Fruit", "Vlees");
        filterProductSoort_choice.setItems(FXCollections.observableArrayList(productSoorten));
        if (filterProductSoort_choice.getSelectionModel().getSelectedItem() == null) {
            filterProductSoort_choice.setValue("Alles");
        }

        for (Tip tip : tipList) {
            tips_table.getItems().add(tip);
        }
    }

    //Database inladen in tips toevoegen als deze leeg is (puur voor functionaliteit weer te geven)
    private void loadDb() {
        CouchDbClient dbClient = new CouchDbClient();
        this.tipList = dbClient.view("_all_docs").includeDocs(true).query(Tip.class);
        if (tipList.isEmpty()) {
            Tip tip1 = new Tip("Dean", "Schorseneren", "Groenten", "https://dagelijksekost.een.be/gerechten/kotelet-met-schorseneren-in-witte-saus");
            Response reponse1 = dbClient.save(tip1);
            tipList.add(tip1);
            if (reponse1.getError() != null) {
                showAlert("Warning", "De tip is niet toegevoegd, probeer het opnieuw");
            }
            Tip tip2 = new Tip("Jonas", "Pastinaak", "Groenten", "https://groentegroente.nl/groentes/pastinaak-bereiden");
            Response reponse2 = dbClient.save(tip2);
            tipList.add(tip2);
            if (reponse2.getError() != null) {
                showAlert("Warning", "De tip is niet toegevoegd, probeer het opnieuw");
            }
        }
        dbClient.shutdown();
        insertInToTable();
    }

    // Open URL in browser
    private void openWebsiteMetDezeURL(String websiteURL) {
        if (checkIfTipIsURL(websiteURL)) {
            Desktop desktop = java.awt.Desktop.getDesktop();
            try {
                //specify the protocol along with the URL
                URI oURL = new URI(websiteURL);
                desktop.browse(oURL);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Warning", "De tip van dit product is geen URL");
        }
    }

    // controleert of meegegeven tip URL wel degelijk een URL is (https://www.youtube.com/watch?v=0sn3nobe6YE)
    public boolean checkIfTipIsURL(String websiteURL) {
        String urlRegex = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(websiteURL);
        return matcher.find();
    }

    //Warning pop-up
    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
