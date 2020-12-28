package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorAfhalingenTableView;
import be.kuleuven.csa.jdbi.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AfTeHalenPakkettenKlantController {
    public TableView<DataVoorAfhalingenTableView> afTeHalenPakkettenKlant_table;
    private String klantNaam;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;

    public void initialize() throws IOException {
        setUpRepo();

        afTeHalenPakkettenKlant_table.getColumns().clear();

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

        auteurRepository = new AuteurRepositoryJdbi3Impl(jdbi);
        klantRepository = new KlantRepositoryJdbi3Impl(jdbi);
        pakketRepository = new PakketRepositoryJdbi3Impl(jdbi);
        boerRepository = new BoerRepositoryJdbi3Impl(jdbi);
        verkooptRepository = new VerkooptRepositoryJdbi3Impl(jdbi);
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void getKlantNaam(String klantNaam) {
        this.klantNaam = klantNaam;
        refreshTable();
    }
}
