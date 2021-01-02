package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.HaaltAf;
import be.kuleuven.csa.domain.Pakket;
import be.kuleuven.csa.domain.ZitIn;
import be.kuleuven.csa.domain.ZitInRepository;
import be.kuleuven.csa.domain.helpdomain.DataVoorZitInTableView;
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

    @Override
    public List<ZitIn> getAlleZitInByProductID(int product_id) {
        var query = "SELECT * FROM ZitIn WHERE product_id = " + product_id + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(ZitIn.class)
                    .list();
        });
    }

    @Override
    public List<DataVoorZitInTableView> getAlleZitVoorTableViewInByVerkoopIDAndWeeknr(int verkoop_id, int weeknr) {
        var query = "SELECT p.product_naam, z.zitIn_hoeveelheid FROM Product p, ZitIn z WHERE z.verkoopt_id = " + verkoop_id + " AND z.product_id = p.product_id AND z.zitIn_weeknr = " + weeknr + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(DataVoorZitInTableView.class)
                    .list();
        });
    }

    @Override
    public void voegZitInToe(ZitIn zitIn) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO ZitIn(product_id, verkoopt_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (?,?,?,?);")
                    .bind(0, zitIn.getProduct_id())
                    .bind(1, zitIn.getVerkoopt_id())
                    .bind(2, zitIn.getZitIn_hoeveelheid())
                    .bind(3, zitIn.getZitIn_weeknr())
                    .execute();
        });
    }

    @Override
    public void verwijderZitInByVerkooptID(int verkoopt_id) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM ZitIn WHERE verkoopt_id = " + verkoopt_id + ";")
                    .execute();
        });
    }


}
