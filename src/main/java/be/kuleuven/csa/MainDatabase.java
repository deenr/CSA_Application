package be.kuleuven.csa;

import be.kuleuven.csa.domain.Auteur;
import be.kuleuven.csa.domain.AuteurRepository;
import be.kuleuven.csa.domain.KlantRepository;
import be.kuleuven.csa.domain.PakketRepository;
import be.kuleuven.csa.jdbi.AuteurRepositoryJdbi3Impl;
import be.kuleuven.csa.jdbi.ConnectionManager;
import be.kuleuven.csa.jdbi.KlantRepositoryJdbi3Impl;
import be.kuleuven.csa.jdbi.PakketRepositoryJdbi3Impl;
import com.sun.tools.javac.Main;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainDatabase {
    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    public final static String DatabasePath = "D:\\Coding\\DAB\\CSA_Application\\csa_database.db";

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


    public static void main(String[] args) throws IOException, URISyntaxException {
        //CouchDbClient couchDbClient = new CouchDbClient();
        setUpRepo();

        //System.out.println(pakketRepository.getAllePakketten());

        System.out.println(pakketRepository.getPakketByName("Dean"));

        //auteurRepository.saveNewAuteur(auteur);

        //System.out.println(auteurRepository.getAllAuteurs().toString());

    }
}
