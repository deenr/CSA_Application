package be.kuleuven.csa.controller;

import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.domain.helpdomain.Tip;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.lightcouch.CouchDbClient;

import java.io.IOException;
import java.util.List;

public class TipsMainController {
    public TableView<Tip> tips_table;

    public void initialize() throws IOException {

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


        refreshTable();
    }

    private void refreshTable() {
        tips_table.getItems().clear();
        CouchDbClient dbClient = new CouchDbClient();

        List<Tip> tipList = dbClient.view("_all_docs").includeDocs(true).query(Tip.class);

        dbClient.shutdown();
        
        for (Tip tip : tipList) {
            tips_table.getItems().add(tip);
        }
    }
}
