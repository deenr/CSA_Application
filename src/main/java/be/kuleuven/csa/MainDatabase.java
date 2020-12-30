package be.kuleuven.csa;

import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.jdbi.*;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainDatabase {
    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static BoerRepository boerRepository;
    private static VerkooptRepository verkooptRepository;
    private static ZitInRepository zitInRepository;
    private static ProductRepository productRepository;

    public final static String DatabasePath = "D:\\Coding\\DAB\\CSA_Application\\csa_database.db";
    public final static String CouchDB = "csa_tipjes";
    public final static String CouchDBUsername = "admin";
    public final static String CouchDBPassword = "admin";

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
        zitInRepository = new ZitInRepositoryJdbi3Impl(jdbi);
        productRepository = new ProductRepositoryJdbi3Impl(jdbi);
    }


    public static void main(String[] args) throws IOException {
        //CouchDbClient couchDbClient = new CouchDbClient();
        setUpRepo();
        
        System.out.println(zitInRepository.getAlleZitVoorTableViewInByVerkoopIDAndWeeknr(8,1).toString());
    }
}
