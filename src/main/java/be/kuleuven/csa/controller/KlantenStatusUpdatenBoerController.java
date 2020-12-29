package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.DataVoorVeranderdStatusPakket;
import be.kuleuven.csa.jdbi.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.*;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class KlantenStatusUpdatenBoerController {
    public TextField statusKlantNaamBoer_text;
    public TextField statusPakketFormaatBoer_text;
    public TextField statusPakketWeeknummerBoer_text;
    public CheckBox statusPakketAfgehaaldBoer_checkBox;
    public Button statusConfirmPakketAfgehaaldBoer_button;
    public TableView<DataVoorVeranderdStatusPakket> afTeHalenPakkettenBoer_table;
    private String boerNaam;

    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;

    public void initialize() throws IOException {
        setUpRepo();

        //BUTTON & MOUSE actions
        statusConfirmPakketAfgehaaldBoer_button.setOnAction(e -> updateStatusVanPakket());
        afTeHalenPakkettenBoer_table.setOnMouseClicked(e -> refreshSelectedData());

        //TABEL
        afTeHalenPakkettenBoer_table.getColumns().clear();
        afTeHalenPakkettenBoer_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DataVoorVeranderdStatusPakket, String> colNaam = new TableColumn<>("Klant naam");
        colNaam.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAuteur_naam()));
        afTeHalenPakkettenBoer_table.getColumns().add(colNaam);

        TableColumn<DataVoorVeranderdStatusPakket, String> colBoer = new TableColumn<>("Pakket soort");
        colBoer.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_naam()));
        afTeHalenPakkettenBoer_table.getColumns().add(colBoer);

        TableColumn<DataVoorVeranderdStatusPakket, Integer> colPrijs = new TableColumn<>("Weeknummer");
        colPrijs.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_weeknr()));
        afTeHalenPakkettenBoer_table.getColumns().add(colPrijs);

        TableColumn<DataVoorVeranderdStatusPakket, Integer> colAantalVolwassenen = new TableColumn<>("Afgehaald");
        colAantalVolwassenen.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPakket_afgehaald()));
        afTeHalenPakkettenBoer_table.getColumns().add(colAantalVolwassenen);

        refreshTable();
    }

    // Aanduiding van afgehaald voor pakket
    private void updateStatusVanPakket() {
        DataVoorVeranderdStatusPakket dataVoorVeranderdStatusPakket = afTeHalenPakkettenBoer_table.getSelectionModel().getSelectedItem();
        if (dataVoorVeranderdStatusPakket == null) {
            showAlert("Warning", "Gelieve een pakket te selecteren");
        } else {
            if (statusPakketAfgehaaldBoer_checkBox.isSelected()) {
                List<Boer> boerList = boerRepository.getBoerByName(boerNaam);
                int boer_id = boerList.get(0).getAuteur_id();

                int pakket_id = 0;
                if (statusPakketFormaatBoer_text.getText().equals("Mediumpakket")) {
                    pakket_id = 1;
                } else if (statusPakketFormaatBoer_text.getText().equals("Grootpakket")) {
                    pakket_id = 2;
                } else if (statusPakketFormaatBoer_text.getText().equals("Familiepakket")) {
                    pakket_id = 3;
                }

                List<Verkoopt> verkooptList = verkooptRepository.getVerkooptByBoerIDAndPakketID(boer_id, pakket_id);
                int verkoopt_id = verkooptList.get(0).getVerkoopt_id();

                List<Klant> klantList = klantRepository.getKlantByName(statusKlantNaamBoer_text.getText());
                int klant_id = klantList.get(0).getAuteur_id();

                List<HaaltAf> haaltAfList = verkooptRepository.getHaaltAfByKlantIDEnVerkooptID(klant_id, verkoopt_id);
                if (haaltAfList.isEmpty()) {
                    showAlert("Warning", "Er is iets mis gegaan in de database");
                } else {
                    HaaltAf haaltAf = haaltAfList.get(0);
                    haaltAf.setPakket_afgehaald(1);

                    verkooptRepository.wijzigHaaltAf(haaltAf);
                    refreshTable();

                    statusKlantNaamBoer_text.setText("");
                    statusPakketFormaatBoer_text.setText("");
                    statusPakketWeeknummerBoer_text.setText("");
                    statusPakketAfgehaaldBoer_checkBox.setSelected(false);
                }
            } else {
                showAlert("Warning", "Gelieve de status van het pakket te wijzigen");
            }
        }
    }

    // Textboxen aanpassen afhankelijk van geklikte rij in tableview
    private void refreshSelectedData() {
        DataVoorVeranderdStatusPakket dataVoorVeranderdStatusPakket = afTeHalenPakkettenBoer_table.getSelectionModel().getSelectedItem();
        if (dataVoorVeranderdStatusPakket != null) {
            statusKlantNaamBoer_text.setText(dataVoorVeranderdStatusPakket.getAuteur_naam());
            statusPakketFormaatBoer_text.setText(dataVoorVeranderdStatusPakket.getPakket_naam());
            statusPakketWeeknummerBoer_text.setText(dataVoorVeranderdStatusPakket.getPakket_weeknr() + "");
        }
    }

    public void refreshTable() {
        afTeHalenPakkettenBoer_table.getItems().clear();

        List<DataVoorVeranderdStatusPakket> veranderdStatusPakketList = pakketRepository.getDataForVeranderingStatusTableViewByBoerName(boerNaam);
        for (DataVoorVeranderdStatusPakket statusPakket : veranderdStatusPakketList) {
            afTeHalenPakkettenBoer_table.getItems().add(statusPakket);
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

        klantRepository = new KlantRepositoryJdbi3Impl(jdbi);
        pakketRepository = new PakketRepositoryJdbi3Impl(jdbi);
        boerRepository = new BoerRepositoryJdbi3Impl(jdbi);
        verkooptRepository = new VerkooptRepositoryJdbi3Impl(jdbi);
    }

    //Warning pop-up
    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Mee gegeven boernaam vorig scherm
    public void getBoerNaam(String boerNaam) {
        this.boerNaam = boerNaam;
        refreshTable();
    }
}
