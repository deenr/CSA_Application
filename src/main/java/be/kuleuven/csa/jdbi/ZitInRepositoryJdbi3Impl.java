package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Pakket;
import be.kuleuven.csa.domain.ZitIn;
import be.kuleuven.csa.domain.ZitInRepository;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ZitInRepositoryJdbi3Impl implements ZitInRepository {

    private final Jdbi jdbi;

    public ZitInRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public List<ZitIn> getAlleZitInByVerkoopID(int verkoop_id) {
        var query = "SELECT * FROM ZitIn WHERE verkoopt_id = " + verkoop_id + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(ZitIn.class)
                    .list();
        });
    }
}
