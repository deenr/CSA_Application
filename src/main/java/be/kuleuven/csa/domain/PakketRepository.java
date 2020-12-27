package be.kuleuven.csa.domain;

import java.util.List;

public interface PakketRepository {

    List<Pakket> getAllePakketten();
    List<Pakket> getPakketByKlantName(String naam);
}
