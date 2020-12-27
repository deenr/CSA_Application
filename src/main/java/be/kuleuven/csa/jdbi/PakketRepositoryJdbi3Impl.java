package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Klant;
import be.kuleuven.csa.domain.Pakket;
import be.kuleuven.csa.domain.PakketRepository;
import be.kuleuven.csa.domain.helpdomain.PakketBoerVoorTable;
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
    public List<Pakket> getPakketByKlantName(String naam) {
        var query = "SELECT p.pakket_id, p.pakket_naam, p.pakket_aantalVolwassenen, p.pakket_aantalKinderen FROM Auteur a, Klant k, SchrijftIn s, Verkoopt v, Pakket p WHERE auteur_naam = '" + naam + "' AND a.auteur_id = k.auteur_id AND k.auteur_id = s.auteur_id AND s.verkoopt_id = v.verkoopt_id AND v.pakket_id = p.pakket_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Pakket.class)
                    .list();
        });
    }

    @Override
    public List<PakketBoerVoorTable> getPakketAndBoerByKlantName(String naam) {
        var query = "SELECT p.pakket_id, p.pakket_naam, a2.auteur_naam, p.pakket_aantalVolwassenen, p.pakket_aantalKinderen FROM Auteur a1, Auteur a2, Boer b, Klant k, SchrijftIn s, Verkoopt v, Pakket p WHERE a1.auteur_naam = '"+naam+"' AND a1.auteur_id = k.auteur_id AND k.auteur_id = s.auteur_id AND s.verkoopt_id = v.verkoopt_id AND v.pakket_id = p.pakket_id AND v.auteur_id = b.auteur_id AND b.auteur_id = a2.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(PakketBoerVoorTable.class)
                    .list();
        });
    }

    public void updatePakket(Pakket pakket) {
    }
}
