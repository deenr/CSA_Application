package be.kuleuven.csa.domain;

import java.util.ArrayList;
import java.util.List;

public interface KlantRepository {

    List<Klant> getAlleKlanten();
    List<Klant> getKlantByName(String naam);
    void saveNewKlant(Klant klant);
    void updateKlant(Klant klant);
}
