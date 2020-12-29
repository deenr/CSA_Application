package be.kuleuven.csa.controller;

import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.domain.helpdomain.Tip;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.lightcouch.CouchDbClient;

import java.util.Arrays;
import java.util.List;

public class TipsMainController {
    public List<Tip> tipList;

    public TableView<Tip> tips_table;
    public ChoiceBox<String> filterProductSoort_choice;
    public Button applyFilterProductSoort_button;
    public Button resetFilterProductSoort_button;

    public void initialize() {
        loadDb();
        applyFilterProductSoort_button.setOnAction(e -> applyFilterProductSoort());
        resetFilterProductSoort_button.setOnAction(e -> resetFilterProductSoort());

        tips_table.getColumns().clear();

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

    private void loadDb() {
        CouchDbClient dbClient = new CouchDbClient();
        this.tipList = dbClient.view("_all_docs").includeDocs(true).query(Tip.class);
        dbClient.shutdown();
        insertInToTable();
    }
}
