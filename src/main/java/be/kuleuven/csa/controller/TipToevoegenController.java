package be.kuleuven.csa.controller;

import be.kuleuven.csa.MainDatabase;
import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.DataVoorTipTableView;
import be.kuleuven.csa.jdbi.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class TipToevoegenController {
    public String auteurNaam;

    public TextField tipURL_text;
    public Button tipToevoegen_button;
    public ChoiceBox<String> selecteerProductVanTip_choice;
    public ChoiceBox<String> selecteerProductSoortVanTip_choice;

    public Text product_text;
    public Text productSoort_text;
    public Text tip_text;

    private static ProductRepository productRepository;

    public void initialize() throws IOException {
        setUpRepo();
        beginLayout();

        //BUTTON action
        tipToevoegen_button.setOnAction(e -> tipToevoegen());
        selecteerProductSoortVanTip_choice.setOnAction(event -> eindLayout());

        refreshData();
    }

    public void beginLayout() {
        productSoort_text.setLayoutY(176);
        selecteerProductSoortVanTip_choice.setLayoutY(159);
        product_text.setVisible(false);
        selecteerProductVanTip_choice.setVisible(false);
        tip_text.setLayoutY(226);
        tipURL_text.setLayoutY(209);
        tipToevoegen_button.setLayoutY(258);
    }

    public void eindLayout() {
        productSoort_text.setLayoutY(163);
        selecteerProductSoortVanTip_choice.setLayoutY(146);
        product_text.setLayoutY(214);
        product_text.setVisible(true);
        selecteerProductVanTip_choice.setLayoutY(196);
        selecteerProductVanTip_choice.setVisible(true);
        tip_text.setLayoutY(263);
        tipURL_text.setLayoutY(246);
        tipToevoegen_button.setLayoutY(295);

        String selectedProductSoort = selecteerProductSoortVanTip_choice.getSelectionModel().getSelectedItem();
        List<String> productList = productRepository.getAlleProductenBySoort(selectedProductSoort.toLowerCase());
        selecteerProductVanTip_choice.setItems(FXCollections.observableArrayList(productList));
    }

    //Tip toevoegen aan NoSQL databas
    public void tipToevoegen() {
        String selectedProductSoort = selecteerProductSoortVanTip_choice.getSelectionModel().getSelectedItem();
        String selectedProduct = selecteerProductVanTip_choice.getSelectionModel().getSelectedItem();
        if (tipURL_text.getText().isEmpty() || selectedProductSoort == null || selectedProduct == null) {
            showAlert("Warning", "Gelieve alle velden in te vullen");
        } else {
            CouchDbClient dbClient = new CouchDbClient();
            int product_id = productRepository.getProductByName(selectedProduct).get(0).getProduct_id();
            Tip toeTeVoegenTip = new Tip(product_id, auteurNaam, tipURL_text.getText());
            Response response = dbClient.save(toeTeVoegenTip);

            if (response.getError() == null) {
                dbClient.shutdown();
                Stage stage = (Stage) selecteerProductVanTip_choice.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Warning", "De tip is niet toegevoegd, probeer het opnieuw");
            }
        }
    }

    public void refreshData() {
        List<String> productSoorten = Arrays.asList("Groenten", "Fruit", "Vlees");
        selecteerProductSoortVanTip_choice.setItems(FXCollections.observableArrayList(productSoorten));
    }

    private static void setUpRepo() throws IOException {
        var databaseFile = new String(Files.readAllBytes(Paths.get(MainDatabase.DatabasePath)));
        if (databaseFile.isEmpty()) {
            ConnectionManager connectionManager = new ConnectionManager();
            connectionManager.flushConnection();
        }
        var jdbi = Jdbi.create(ConnectionManager.ConnectionString);
        jdbi.installPlugin(new SqlObjectPlugin());

        productRepository = new ProductRepositoryJdbi3Impl(jdbi);
    }

    //Warning pop-up
    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Mee gegeven auteurnaam vorig scherm
    public void getAuteurNaam(String auteurNaam) {
        this.auteurNaam = auteurNaam;
    }
}
