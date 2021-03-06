package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Pakket;
import be.kuleuven.csa.domain.PakketRepository;
import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorAfhalingenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorPakketTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorVeranderdStatusPakket;
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
    public List<DataVoorPakketTableView> getAllePakkettenVoorDataView() {
        var query = "SELECT a1.auteur_naam as boer_naam, p.pakket_naam, a2.auteur_naam as klant_naam, h.pakket_weeknr, h.pakket_afgehaald FROM Auteur a1, Auteur a2, Pakket p, HaaltAf h, Verkoopt v WHERE a1.auteur_id = v.auteur_id AND v.verkoopt_id = h.verkoopt_id AND v.pakket_id = p.pakket_id AND h.auteur_id = a2.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(DataVoorPakketTableView.class)
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
    public List<DataVoorAbonnementenTableView> getDataForAbonnementenTableViewByKlantName(String naam) {
        var query = "SELECT p.pakket_id, p.pakket_naam, a2.auteur_naam, v.verkoopt_prijs, p.pakket_aantalVolwassenen, p.pakket_aantalKinderen FROM Auteur a1, Auteur a2, Boer b, Klant k, SchrijftIn s, Verkoopt v, Pakket p WHERE a1.auteur_naam = '" + naam + "' AND a1.auteur_id = k.auteur_id AND k.auteur_id = s.auteur_id AND s.verkoopt_id = v.verkoopt_id AND v.pakket_id = p.pakket_id AND v.auteur_id = b.auteur_id AND b.auteur_id = a2.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(DataVoorAbonnementenTableView.class)
                    .list();
        });
    }

    @Override
    public List<DataVoorAfhalingenTableView> getDataForAfhalingenTableViewByKlantName(String naam) {
        var query = "SELECT p.pakket_naam, a2.auteur_naam, h.pakket_weeknr, b.boer_adres FROM Auteur a1, Auteur a2, Boer b, Klant k, HaaltAf h, Verkoopt v, Pakket p WHERE a1.auteur_naam = '" + naam + "' AND a1.auteur_id = k.auteur_id AND k.auteur_id = h.auteur_id AND h.verkoopt_id = v.verkoopt_id  AND v.pakket_id = p.pakket_id AND v.auteur_id = b.auteur_id AND b.auteur_id = a2.auteur_id AND h.pakket_afgehaald = 0";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(DataVoorAfhalingenTableView.class)
                    .list();
        });
    }

    @Override
    public List<String> getPakkettenListByBoerNameAndKlantName(String boerNaam, String klantNaam) {
        var query = "SELECT p.pakket_naam FROM Auteur a1, Auteur a2, SchrijftIn s, Verkoopt v, Pakket p WHERE a1.auteur_naam = '" + klantNaam + "' and a1.auteur_id = s.auteur_id and s.verkoopt_id = v.verkoopt_id and v.pakket_id = p.pakket_id and a2.auteur_naam = '" + boerNaam + "' and a2.auteur_id = v.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapTo(String.class)
                    .list();
        });
    }

    @Override
    public List<DataVoorVeranderdStatusPakket> getDataForVeranderingStatusTableViewByBoerName(String naam) {
        var query = "SELECT a1.auteur_naam, p.pakket_naam, h.pakket_weeknr, h.pakket_afgehaald FROM Auteur a1, Auteur a2, Pakket p, HaaltAf h, Verkoopt v WHERE a2.auteur_naam = '" + naam + "' and a2.auteur_id = v.auteur_id and v.pakket_id = p.pakket_id and v.verkoopt_id = h.verkoopt_id and h.auteur_id = a1.auteur_id and h.pakket_afgehaald = 0";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(DataVoorVeranderdStatusPakket.class)
                    .list();
        });
    }

    public void updatePakket(Pakket pakket) {
    }
}
