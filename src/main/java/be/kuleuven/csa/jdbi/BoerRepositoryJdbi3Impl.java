package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Auteur;
import be.kuleuven.csa.domain.Boer;
import be.kuleuven.csa.domain.BoerRepository;
import be.kuleuven.csa.domain.helpdomain.DataVoorBoerTableView;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class BoerRepositoryJdbi3Impl implements BoerRepository {

    private final Jdbi jdbi;

    public BoerRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public List<DataVoorBoerTableView> getAlleBoerenVoorDataView() {
        var query = "SELECT a.auteur_naam, b.boer_adres FROM Auteur a JOIN Boer b ON a.auteur_id = b.auteur_id\n";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(DataVoorBoerTableView.class)
                    .list();
        });
    }

    @Override
    public List<String> getAlleBoerNamen() {
        var query = "SELECT a.auteur_naam FROM Boer b, Auteur a WHERE b.auteur_id = a.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapTo(String.class)
                    .list();
        });
    }

    @Override
    public List<Boer> getBoerByName(String naam) {
        var query = "SELECT b.auteur_id, b.boer_adres FROM Auteur a, Boer b WHERE auteur_naam = '" + naam + "'AND a.auteur_id = b.auteur_id";
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
            handle.createUpdate("UPDATE Boer SET boer_adres = ? WHERE auteur_id = ?")
                    .bind(0, boer.getBoer_adres())
                    .bind(1, boer.getAuteur_id())
                    .execute();
        });
    }
}
