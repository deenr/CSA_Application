package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.*;
import be.kuleuven.csa.domain.helpdomain.WijzigHaaltAf;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class VerkooptRepositoryJdbi3Impl implements VerkooptRepository {

    private final Jdbi jdbi;

    public VerkooptRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }


    @Override
    public List<Verkoopt> getVerkooptByBoerAndPakket(int auteur_id, int pakket_id) {
        var query = "SELECT * FROM Verkoopt v WHERE v.auteur_id = " + auteur_id + " AND v.pakket_id = " + pakket_id + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Verkoopt.class)
                    .list();
        });
    }

    @Override
    public void wijzigVerkoopt(Verkoopt verkoopt) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE Verkoopt SET verkoopt_prijs = ? WHERE auteur_id = ? AND pakket_id = ?")
                    .bind(0, verkoopt.getVerkoopt_prijs())
                    .bind(1, verkoopt.getAuteur_id())
                    .bind(2, verkoopt.getPakket_id())
                    .execute();
        });

    }

    @Override
    public void wijzigSchrijftIn(SchrijftIn schrijftIn) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE SchrijftIn SET verkoopt_id = ? WHERE auteur_id = ? AND schrijftIn_id = ?")
                    .bind(0, schrijftIn.getVerkoopt_id())
                    .bind(1, schrijftIn.getAuteur_id())
                    .bind(2, schrijftIn.getSchrijftIn_id())
                    .execute();
        });
    }

    @Override
    public void wijzigHaaltAf(HaaltAf haaltAf) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE HaaltAf SET pakket_afgehaald = ? WHERE auteur_id = ? AND verkoopt_id = ?")
                    .bind(0, haaltAf.getPakket_afgehaald())
                    .bind(1, haaltAf.getAuteur_id())
                    .bind(2, haaltAf.getVerkoopt_id())
                    .execute();
        });
    }

    public List<HaaltAf> getHaaltAfByKlantEnVerkoopt(int auteur_id, int verkoopt_id) {
        var query = "SELECT * FROM HaaltAf h WHERE h.verkoopt_id = " + verkoopt_id + " AND h.auteur_id = " + auteur_id + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(HaaltAf.class)
                    .list();
        });
    }

    public List<SchrijftIn> getSchrijftInByKlantEnVerkoopt(int auteur_id, int verkoopt_id) {
        var query = "SELECT * FROM SchrijftIn s WHERE s.verkoopt_id = " + verkoopt_id + " AND s.auteur_id = " + auteur_id + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(SchrijftIn.class)
                    .list();
        });
    }

    @Override
    public List<Verkoopt> getVerkooptByKlantName(String naam) {
        var query = "SELECT v.verkoopt_id, v.auteur_id, v.pakket_id, v.verkoopt_prijs FROM Auteur a, Klant k, SchrijftIn s, Verkoopt v, Pakket p WHERE auteur_naam = '" + naam + "' AND a.auteur_id = k.auteur_id AND k.auteur_id = s.auteur_id AND s.verkoopt_id = v.verkoopt_id AND v.pakket_id = p.pakket_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Verkoopt.class)
                    .list();
        });
    }

    @Override
    public void voegHaaltAfToe(HaaltAf haaltAf) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO HaaltAf(auteur_id, verkoopt_id, pakket_weeknr, pakket_afgehaald) VALUES (?,?,?,?);")
                    .bind(0, haaltAf.getAuteur_id())
                    .bind(1, haaltAf.getVerkoopt_id())
                    .bind(2, haaltAf.getPakket_weeknr())
                    .bind(3, haaltAf.getPakket_afgehaald())
                    .execute();
        });
    }

    @Override
    public void voegSchrijftInToe(SchrijftIn schrijftIn) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO SchrijftIn(auteur_id,verkoopt_id) VALUES (?,?);")
                    .bind(0, schrijftIn.getAuteur_id())
                    .bind(1, schrijftIn.getVerkoopt_id())
                    .execute();
        });
    }

    @Override
    public void verwijderHaaltAf(HaaltAf haaltAf) {
        int auteur_id = haaltAf.getAuteur_id();
        int verkoopt_id = haaltAf.getVerkoopt_id();
        int pakket_weeknr = haaltAf.getPakket_weeknr();
        int pakket_afgehaald = haaltAf.getPakket_afgehaald();
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM HaaltAf WHERE auteur_id = " + auteur_id + " AND verkoopt_id = " + verkoopt_id + " ;")
                    .execute();
        });
    }

    @Override
    public void verwijderSchrijftIn(SchrijftIn schrijftIn) {
        int auteur_id = schrijftIn.getAuteur_id();
        int verkoopt_id = schrijftIn.getVerkoopt_id();
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM SchrijftIn WHERE auteur_id = " + auteur_id + " AND verkoopt_id = " + verkoopt_id + " ;")
                    .execute();
        });
    }

    @Override
    public void maakNieuweVerkooptAan(Verkoopt verkoopt) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO Verkoopt (auteur_id, pakket_id, verkoopt_prijs) VALUES (?,?,?);")
                    .bind(0, verkoopt.getAuteur_id())
                    .bind(1, verkoopt.getPakket_id())
                    .bind(2, verkoopt.getVerkoopt_prijs())
                    .execute();
        });
    }

    @Override
    public List<Integer> getVerkooptPrijzenByName(String naam) {
        var query = "SELECT v.verkoopt_prijs FROM Auteur a, Klant k, SchrijftIn s, Verkoopt v WHERE a.auteur_naam = '" + naam + "' AND a.auteur_id = k.auteur_id and k.auteur_id = s.auteur_id and s.verkoopt_id = v.verkoopt_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapTo(Integer.class)
                    .list();
        });
    }

    @Override
    public List<Integer> getVerkooptPrijzenByKlantAndByBoer(String klantNaam, String boerNaam) {
        var query = "SELECT v.verkoopt_prijs FROM Auteur a1, Auteur a2, SchrijftIn s, Verkoopt v, Pakket p WHERE a1.auteur_naam = '" + klantNaam + "' and a1.auteur_id = s.auteur_id and s.verkoopt_id = v.verkoopt_id and v.pakket_id = p.pakket_id and a2.auteur_naam = '" + boerNaam + "' and a2.auteur_id = v.auteur_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapTo(Integer.class)
                    .list();
        });
    }
}
