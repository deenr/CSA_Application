package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Auteur;
import be.kuleuven.csa.domain.AuteurRepository;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class AuteurRepositoryJdbi3Impl implements AuteurRepository {

    private final Jdbi jdbi;

    public AuteurRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public List<Auteur> getAllAuteurs() {
        var query = "SELECT * FROM Auteur";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Auteur.class)
                    .list();
        });
    }

    @Override
    public List<Auteur> getAuteurByName(String naam) {
        var query = "SELECT * FROM Auteur WHERE auteur_naam = '" + naam + "'";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Auteur.class)
                    .list();
        });
    }

    @Override
    public List<Auteur> getAuteurByID(int id) {
        var query = "SELECT * FROM Auteur WHERE auteur_id = '" + id + "'";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Auteur.class)
                    .list();
        });
    }

    @Override
    public void saveNewAuteur(Auteur auteur) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO Auteur(auteur_naam) VALUES (?)")
                    .bind(0, auteur.getAuteur_naam())
                    .execute();
        });
    }

    @Override
    public void updateAuteur(Auteur auteur) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE Auteur SET auteur_naam = ?")
                    .bind(0, auteur.getAuteur_naam())
                    .execute();
        });
    }

    @Override
    public void verwijderAuteurByAuteurID(String auteur_id) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM Auteur WHERE auteur_id = " + auteur_id + ";")
                    .execute();
        });
    }
}
