package be.kuleuven.csa.domain;

import be.kuleuven.csa.domain.helpdomain.PakketBoerVoorTable;

import java.util.List;

public interface PakketRepository {

    List<Pakket> getAllePakketten();
    List<Pakket> getPakketByKlantName(String naam);
    List<PakketBoerVoorTable> getPakketAndBoerByKlantName(String naam);
}
