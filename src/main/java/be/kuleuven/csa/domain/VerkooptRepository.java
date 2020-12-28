package be.kuleuven.csa.domain;

import be.kuleuven.csa.domain.helpdomain.WijzigHaaltAf;

import java.util.List;

public interface VerkooptRepository {

    List<Verkoopt> getVerkooptByBoerAndPakket(int auteur_id, int pakket_id);
    void wijzigVerkoopt(Verkoopt verkoopt);
    void wijzigSchrijftIn(SchrijftIn schrijftIn);
    void wijzigHaaltAf(WijzigHaaltAf wijzigHaaltAf);
    List<HaaltAf> getHaaltAfByKlantEnVerkoopt(int auteur_id, int verkoopt_id);
    List<SchrijftIn> getSchrijftInByKlantEnVerkoopt(int auteur_id, int verkoopt_id);
    List<Verkoopt> getVerkooptByKlantName(String naam);
    void voegHaaltAfToe(HaaltAf haaltAf);
    void voegSchrijftInToe(SchrijftIn schrijftIn);
    void verwijderHaaltAf(HaaltAf haaltAf);
    void verwijderSchrijftIn(SchrijftIn schrijftIn);
    void maakNieuweVerkooptAan(Verkoopt verkoopt);
    List<Integer> getVerkooptPrijzenByName(String naam);

}
