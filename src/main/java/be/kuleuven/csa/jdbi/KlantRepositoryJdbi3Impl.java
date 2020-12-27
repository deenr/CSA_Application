package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Boer;
import be.kuleuven.csa.domain.Klant;
import be.kuleuven.csa.domain.KlantRepository;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class KlantRepositoryJdbi3Impl implements KlantRepository {

    private final Jdbi jdbi;

    public KlantRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public List<Klant> getAlleKlanten() {
        var query = "SELECT * FROM Klant";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Klant.class)
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
    public void saveNewKlant(Klant klant) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO Klant(auteur_id, klant_teBetalenBedrag) VALUES (?, ?)")
                    .bind(0, klant.getAuteur_id())
                    .bind(1, klant.getKlant_teBetalenBedrag())
                    .execute();
        });
        System.out.println("Klant is met id " + klant.getAuteur_id() + " is in de database geplaatst.");
    }

    @Override
    public void updateKlant(Klant klant) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE Klant SET klant_teBetalenBedrag = ?")
                    .bind(0, klant.getKlant_teBetalenBedrag())
                    .execute();
        });
    }
}
