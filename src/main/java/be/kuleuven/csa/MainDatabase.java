package be.kuleuven.csa;

import be.kuleuven.csa.domain.Auteur;
import be.kuleuven.csa.domain.AuteurRepository;
import be.kuleuven.csa.jdbi.AuteurRepositoryJdbi3Impl;
import be.kuleuven.csa.jdbi.ConnectionManager;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.lightcouch.CouchDbClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class MainDatabase {
    private static AuteurRepository auteurRepository;

    private static void setUpRepo() throws IOException {

        var databaseFile = new String(Files.readAllBytes(Paths.get("D:\\School\\Intellij\\CSA_Application\\csa_database.db")));
        if (databaseFile.isEmpty()){
            ConnectionManager connectionManager = new ConnectionManager();
            connectionManager.flushConnection();
        }
        var jdbi = Jdbi.create(ConnectionManager.ConnectionString);
        jdbi.installPlugin(new SqlObjectPlugin());

        auteurRepository = new AuteurRepositoryJdbi3Impl(jdbi);
    }


    public static void main(String[] args) throws IOException {
        //CouchDbClient couchDbClient = new CouchDbClient();
        setUpRepo();

        //Auteur auteur = new Auteur("Pierertje");

        //auteurRepository.saveNewAuteur(auteur);

        System.out.println(auteurRepository.getAllAuteurs().toString());

    }
}
