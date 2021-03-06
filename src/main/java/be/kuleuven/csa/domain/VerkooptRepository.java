package be.kuleuven.csa.domain;

import be.kuleuven.csa.domain.helpdomain.WijzigHaaltAf;

import java.util.List;

public interface VerkooptRepository {

    List<Verkoopt> getVerkooptByBoerIDAndPakketID(int auteur_id, int pakket_id);

    void wijzigVerkoopt(Verkoopt verkoopt);

    void wijzigSchrijftIn(SchrijftIn schrijftIn);

    void wijzigHaaltAf(HaaltAf haaltAf);

    List<HaaltAf> getHaaltAfByKlantIDEnVerkooptID(int auteur_id, int verkoopt_id);

    List<SchrijftIn> getSchrijftInByKlantIDEnVerkooptID(int auteur_id, int verkoopt_id);

    List<Verkoopt> getVerkooptByKlantName(String naam);

    void voegHaaltAfToe(HaaltAf haaltAf);

    void voegSchrijftInToe(SchrijftIn schrijftIn);

    void verwijderHaaltAf(HaaltAf haaltAf);

    void verwijderSchrijftIn(SchrijftIn schrijftIn);

    void maakNieuweVerkooptAan(Verkoopt verkoopt);

    List<Integer> getVerkooptPrijzenByName(String naam);

    List<Integer> getVerkooptPrijzenByKlantNameAndByBoerName(String klantNaam, String boerNaam);

    List<Verkoopt> getVerkooptByBoerID(int auteur_id);

    void verwijderHaaltAfByAuteurID(int auteur_id);

    void verwijderSchrijftInByAuteurID(int auteur_id);

    void verwijderVerkooptByAuteurID(int auteur_id);
}
