package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Boer;
import be.kuleuven.csa.domain.Klant;
import be.kuleuven.csa.domain.KlantRepository;
import be.kuleuven.csa.domain.helpdomain.DataVoorKlantTableView;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class KlantRepositoryJdbi3Impl implements KlantRepository {

    private final Jdbi jdbi;

    public KlantRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public List<DataVoorKlantTableView> getAlleKlantenVoorDataView() {
        var query = "SELECT a.auteur_naam, k.klant_teBetalenBedrag FROM Auteur a JOIN Klant k ON a.auteur_id = k.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(DataVoorKlantTableView.class)
                    .list();
        });
    }

    @Override
    public List<Klant> getKlantByName(String naam) {
        var query = "SELECT k.auteur_id, k.klant_teBetalenBedrag FROM Auteur a, Klant k WHERE auteur_naam = '" + naam + "' AND a.auteur_id = k.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Klant.class)
                    .list();
        });
    }

    @Override
    public List<Klant> getKlantByVerkooptID(int id) {
        var query = "SELECT k.auteur_id, k.klant_teBetalenBedrag FROM SchrijftIn s, Klant k WHERE '" + id + "' = s.verkoopt_id and s.auteur_id = k.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Klant.class)
                    .list();
        });
    }

    @Override
    public List<String> getKlantenByBoerName(String naam) {
        var query = "SELECT DISTINCT a2.auteur_naam FROM Boer b, Verkoopt v, SchrijftIn s, Auteur a1, Auteur a2 WHERE a1.auteur_naam = '" + naam + "'  and a1.auteur_id = v.auteur_id and v.verkoopt_id = s.verkoopt_id and s.auteur_id = a2.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapTo(String.class)
                    .list();
        });
    }

    @Override
    public void saveNewKlant(Klant klant) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO Klant(auteur_id, klant_teBetalenBedrag) VALUES (?, ?)")
                    .bind(0, klant.getAuteur_id())
                    .bind(1, klant.getKlant_teBetalenBedrag())
                    .execute();
        });
    }

    @Override
    public void updateKlant(Klant klant) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE Klant SET klant_teBetalenBedrag = ? WHERE auteur_id = ?")
                    .bind(0, klant.getKlant_teBetalenBedrag())
                    .bind(1, klant.getAuteur_id())
                    .execute();
        });
    }

    @Override
    public void verwijderKlantByAuteurID(int auteur_id) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM Klant WHERE auteur_id = " + auteur_id + ";")
                    .execute();
        });
    }
}
