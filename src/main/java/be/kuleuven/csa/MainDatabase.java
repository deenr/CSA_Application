package be.kuleuven.csa;

import be.kuleuven.csa.domain.AuteurRepository;
import be.kuleuven.csa.jdbi.AuteurRepositoryJdbi3Impl;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.lightcouch.CouchDbClient;

import java.sql.SQLException;

public class MainDatabase {
    private static AuteurRepository auteurRepository;

    private static void setUpRepo() {
        var jdbi = Jdbi.create("jdbc:sqlite:csa_database.db");
        jdbi.installPlugin(new SqlObjectPlugin());

        auteurRepository = new AuteurRepositoryJdbi3Impl(jdbi);
    }


    public static void main(String[] args) {
        //CouchDbClient couchDbClient = new CouchDbClient();
        setUpRepo();


    }
}
