package be.kuleuven.csa;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CSAMain extends Application {
    private static Stage rootStage;

    public static Stage getRootStage() {
        return rootStage;
    }
    @Override
    public void start(Stage stage) throws Exception {
        rootStage = stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("begin_scherm.fxml"));

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("CSA Applicatie");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
