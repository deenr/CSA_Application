package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.AuteurRepository;
import be.kuleuven.csa.domain.KlantRepository;
import be.kuleuven.csa.domain.Pakket;
import be.kuleuven.csa.domain.PakketRepository;
import be.kuleuven.csa.jdbi.AuteurRepositoryJdbi3Impl;
import be.kuleuven.csa.jdbi.ConnectionManager;
import be.kuleuven.csa.jdbi.KlantRepositoryJdbi3Impl;
import be.kuleuven.csa.jdbi.PakketRepositoryJdbi3Impl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BestaandeKlantController {
    public Button nieuwPakketBestellen_button;
    public Button annuleerPakket_button;
    public Button wijzigPakket_button;
    public TableView bestaandeKlantPakketten_Tbl;

    public String klantNaam;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;

    public void initialize() throws IOException {
        setUpRepo();

        bestaandeKlantPakketten_Tbl.getColumns().clear();

        TableColumn<Pakket, Integer> colPakket_id = new TableColumn<>("Pakket_id");
        colPakket_id.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_id()));
        bestaandeKlantPakketten_Tbl.getColumns().add(colPakket_id);

        TableColumn<Pakket, String> colNaam = new TableColumn<>("Naam");
        colNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_naam()));
        bestaandeKlantPakketten_Tbl.getColumns().add(colNaam);

        TableColumn<Pakket, Integer> colAantalVolwassenen = new TableColumn<>("Aantal Volwassenen");
        colAantalVolwassenen.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_aantalVolwassenen()));
        bestaandeKlantPakketten_Tbl.getColumns().add(colAantalVolwassenen);

        TableColumn<Pakket, Integer> colAantalKinderen = new TableColumn<>("Aantal Kinderen");
        colAantalKinderen.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_aantalKinderen()));
        bestaandeKlantPakketten_Tbl.getColumns().add(colAantalKinderen);

        refreshTable();


    }

    public void refreshTable() {
        bestaandeKlantPakketten_Tbl.getItems().clear();

        System.out.println(klantNaam);

        List<Pakket> pakketList = pakketRepository.getPakketByName(klantNaam);
        System.out.println(pakketList.toString());

        for (Pakket p : pakketList) {
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
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void getNaamVanBestaandeKlant(String naam) {
        this.klantNaam = naam;
        refreshTable();
    }
}
