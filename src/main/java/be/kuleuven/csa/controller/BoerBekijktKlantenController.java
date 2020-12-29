package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.DataVoorAfhalingenTableView;
import be.kuleuven.csa.domain.helpdomain.KlantenBoer;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoerBekijktKlantenController {
    public TableView<KlantenBoer> klantenVanBoer_table;
    private String boerNaam;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;

    public void initialize() throws IOException {
        setUpRepo();

        klantenVanBoer_table.getColumns().clear();

        klantenVanBoer_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<KlantenBoer, String> colKlantenNaam = new TableColumn<>("Klant naam");
        colKlantenNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getNaam()));
        klantenVanBoer_table.getColumns().add(colKlantenNaam);

        TableColumn<KlantenBoer, List<String>> colPakketNaam = new TableColumn<>("Pakketten");
        colPakketNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakketten()));
        klantenVanBoer_table.getColumns().add(colPakketNaam);

        TableColumn<KlantenBoer, Integer> colPrijs = new TableColumn<>("Soort Pakket");
        colPrijs.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPrijs()));
        klantenVanBoer_table.getColumns().add(colPrijs);

        refreshTable();
    }

    public void refreshTable() {
        klantenVanBoer_table.getItems().clear();

        List<String> klantenVanBoerLijst = klantRepository.getKlantenByBoerName(boerNaam);
        ArrayList<KlantenBoer> alleKlantenBoerenList = new ArrayList<>();

        for (String klantNaam : klantenVanBoerLijst){
            List<String> pakkettenVanDeKlant = pakketRepository.getPakkettenListByBoerNameAndKlantName(boerNaam,klantNaam);
            List<Integer> prijzenVanDePakketten= verkooptRepository.getVerkooptPrijzenByKlantAndByBoer(klantNaam, boerNaam);
            int totaleBedrag = 0;
            for (Integer prijs : prijzenVanDePakketten) {
                totaleBedrag = totaleBedrag + prijs;
            }
            KlantenBoer nieuweKlantenBoer = new KlantenBoer(klantNaam,pakkettenVanDeKlant,totaleBedrag);
            alleKlantenBoerenList.add(nieuweKlantenBoer);
        }

        System.out.println(alleKlantenBoerenList.toString());

        for (KlantenBoer klantenBoer : alleKlantenBoerenList) {
            klantenVanBoer_table.getItems().add(klantenBoer);
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

    public void getBoerNaam(String boerNaam) {
        this.boerNaam = boerNaam;
        System.out.println(boerNaam);
        refreshTable();
    }
}
