package be.kuleuven.csa.domain;

import java.util.List;

public interface VerkooptRepository {

    List<Verkoopt> getVerkooptByBoerAndPakket(int auteur_id, int pakket_id);
    void wijzigSchrijftIn(SchrijftIn schrijftIn);
    void wijzigHaaltAf(HaaltAf haaltAf);
    List<HaaltAf> getHaaltAfByKlantEnVerkoopt(int auteur_id, int verkoopt_id);
    List<SchrijftIn> getSchrijftInByKlantEnVerkoopt(int auteur_id, int verkoopt_id);
    List<Verkoopt> getVerkooptByKlantName(String naam);
    void voegHaaltAfToe(HaaltAf haaltAf);
    void voegSchijftInToe(SchrijftIn schrijftIn);
}
