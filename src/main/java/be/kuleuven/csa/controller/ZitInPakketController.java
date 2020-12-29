package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorAfhalingenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorZitInTableView;
import be.kuleuven.csa.jdbi.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ZitInPakketController {
    public DataVoorAfhalingenTableView pakket;
    public TableView<DataVoorZitInTableView> productZinInPakket_table;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static VerkooptRepository verkooptRepository;
    private static BoerRepository boerRepository;
    private static ZitInRepository zitInRepository;

    public void initialize() throws IOException {
        setUpRepo();
        //productZinInPakket_table.setOnMouseClicked(e->refreshItems());
        productZinInPakket_table.getColumns().clear();
        productZinInPakket_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DataVoorZitInTableView, String> colProductNaam = new TableColumn<>("Pakket naam");
        colProductNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getProduct_naam()));
        productZinInPakket_table.getColumns().add(colProductNaam);

        TableColumn<DataVoorZitInTableView, Integer> colProductHoeveelheid = new TableColumn<>("Hoeveelheid");
        colProductHoeveelheid.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getZitIn_hoeveelheid()));
        productZinInPakket_table.getColumns().add(colProductHoeveelheid);

        //refreshItems();
    }

    public void refreshItems() {
        int pakket_id = 0;
        switch (pakket.getPakket_naam()) {
            case "Mediumpakket":
                pakket_id = 1;
                break;
            case "Grootpakket":
                pakket_id = 2;
                break;
            case "Familiepakket":
                pakket_id = 3;
                break;
        }
        int boer_id = boerRepository.getBoerByName(pakket.getAuteur_naam()).get(0).getAuteur_id();
        int verkoopt_id = verkooptRepository.getVerkooptByBoerAndPakket(boer_id, pakket_id).get(0).getVerkoopt_id();
        int weeknr = pakket.getPakket_weeknr();

        List<DataVoorZitInTableView> zitInTableViewList = zitInRepository.getAlleZitVoorTableViewInByVerkoopAndWeeknr(verkoopt_id, weeknr);
        for (DataVoorZitInTableView dataVoorZitInTableView : zitInTableViewList) {
            productZinInPakket_table.getItems().add(dataVoorZitInTableView);
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
        verkooptRepository = new VerkooptRepositoryJdbi3Impl(jdbi);
        boerRepository = new BoerRepositoryJdbi3Impl(jdbi);
        zitInRepository = new ZitInRepositoryJdbi3Impl(jdbi);
    }

    public void getGeselecteerdPakket(DataVoorAfhalingenTableView pakket) {
        this.pakket = pakket;
        System.out.println(pakket.toString());

        refreshItems();
    }
}
