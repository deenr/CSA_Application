package be.kuleuven.csa.controller;

import be.kuleuven.csa.CSAMain;
import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorAfhalingenTableView;
import be.kuleuven.csa.jdbi.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AfTeHalenPakkettenKlantController {
    public TableView<DataVoorAfhalingenTableView> afTeHalenPakkettenKlant_table;
    private String klantNaam;

    private static PakketRepository pakketRepository;

    public void initialize() throws IOException {
        setUpRepo();

        // Tabel
        afTeHalenPakkettenKlant_table.getColumns().clear();
        afTeHalenPakkettenKlant_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        afTeHalenPakkettenKlant_table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && afTeHalenPakkettenKlant_table.getSelectionModel().getSelectedItem() != null) {
                DataVoorAfhalingenTableView afTeHalenPakket = afTeHalenPakkettenKlant_table.getSelectionModel().getSelectedItem();
                showSchermToonInhoudPakket("pakket_zitIn", afTeHalenPakket);
            }
        });

        TableColumn<DataVoorAfhalingenTableView, String> colPakketNaam = new TableColumn<>("Soort Pakket");
        colPakketNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_naam()));
        afTeHalenPakkettenKlant_table.getColumns().add(colPakketNaam);

        TableColumn<DataVoorAfhalingenTableView, String> colBoer = new TableColumn<>("Boer");
        colBoer.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAuteur_naam()));
        afTeHalenPakkettenKlant_table.getColumns().add(colBoer);

        TableColumn<DataVoorAfhalingenTableView, Integer> colWeeknr = new TableColumn<>("Weeknummer");
        colWeeknr.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_weeknr()));
        afTeHalenPakkettenKlant_table.getColumns().add(colWeeknr);

        TableColumn<DataVoorAfhalingenTableView, String> colAdres = new TableColumn<>("Adres van de boer");
        colAdres.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getBoer_adres()));
        afTeHalenPakkettenKlant_table.getColumns().add(colAdres);

        refreshTable();
    }

    public void refreshTable() {
        afTeHalenPakkettenKlant_table.getItems().clear();

        List<DataVoorAfhalingenTableView> afhalingenTableViewList = pakketRepository.getDataForAfhalingenTableViewByKlantName(klantNaam);
        for (DataVoorAfhalingenTableView afhaling : afhalingenTableViewList) {
            afTeHalenPakkettenKlant_table.getItems().add(afhaling);
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

        pakketRepository = new PakketRepositoryJdbi3Impl(jdbi);
    }

    // Meegegeven klantnaam vorig scherm
    public void getKlantNaam(String klantNaam) {
        this.klantNaam = klantNaam;
        refreshTable();
    }

    //Nieuw scherm openen
    private void showSchermToonInhoudPakket(String id, DataVoorAfhalingenTableView afTeHalenPakket) {
        var resourceName = id + ".fxml";
        try {
            var stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            Parent root = (AnchorPane) loader.load();

            ZitInPakketController zitInPakketController = loader.getController();
            zitInPakketController.getGeselecteerdPakket(afTeHalenPakket);

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
}
