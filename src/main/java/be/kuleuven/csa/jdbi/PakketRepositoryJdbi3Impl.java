package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Klant;
import be.kuleuven.csa.domain.Pakket;
import be.kuleuven.csa.domain.PakketRepository;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class PakketRepositoryJdbi3Impl implements PakketRepository {

    private final Jdbi jdbi;

    public PakketRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public List<Pakket> getAllePakketten() {
        var query = "SELECT * FROM Pakket";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Pakket.class)
                    .list();
        });
    }

    @Override
    public List<Pakket> getPakketByName(String naam) {
        var query = "SELECT p.pakket_id, p.pakket_naam, p.pakket_aantalVolwassenen, p.pakket_aantalKinderen FROM Auteur a, Klant k, SchrijftIn s, Verkoopt v, Pakket p WHERE auteur_naam = '" + naam + "' AND a.auteur_id = k.auteur_id AND k.auteur_id = s.auteur_id AND s.verkoopt_id = v.verkoopt_id AND v.pakket_id = p.pakket_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Pakket.class)
                    .list();
        });
    }
}
