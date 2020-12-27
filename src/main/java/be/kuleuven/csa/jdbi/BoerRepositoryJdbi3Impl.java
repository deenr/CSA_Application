package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Auteur;
import be.kuleuven.csa.domain.Boer;
import be.kuleuven.csa.domain.BoerRepository;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class BoerRepositoryJdbi3Impl implements BoerRepository {

    private final Jdbi jdbi;

    public BoerRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }
    @Override
    public List<Boer> getAlleBoeren() {
        var query = "SELECT * FROM Boer";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Boer.class)
                    .list();
        });
    }

    @Override
    public List<Boer> getBoerByName(String naam) {
        var query = "SELECT * FROM Boer WHERE auteur_naam = '" + naam + "'";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Boer.class)
                    .list();
        });
    }

    @Override
    public void saveNewBoer(Boer boer) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO Boer(auteur_id, boer_adres) VALUES (?, ?)")
                    .bind(0, boer.getAuteur_id())
                    .bind(1, boer.getBoer_adres())
                    .execute();
        });
    }

    @Override
    public void updateBoer(Boer boer) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE Boer SET auteur_id = ?, boer_adres = ?")
                    .bind(0, boer.getAuteur_id())
                    .bind(1, boer.getBoer_adres())
                    .execute();
        });
    }
}
