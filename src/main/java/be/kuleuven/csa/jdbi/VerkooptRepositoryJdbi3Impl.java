package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Pakket;
import be.kuleuven.csa.domain.Verkoopt;
import be.kuleuven.csa.domain.VerkooptRepository;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class VerkooptRepositoryJdbi3Impl implements VerkooptRepository {

    private final Jdbi jdbi;

    public VerkooptRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }


    @Override
    public List<Verkoopt> getVerkooptByBoerAndSize(int auteur_id, int pakket_id) {
        var query = "SELECT * FROM Verkoopt v WHERE v.auteur_id = " + auteur_id + " AND v.pakket_id = " + pakket_id + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Verkoopt.class)
                    .list();
        });
    }
}
