package be.kuleuven.csa.domain;

import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorAfhalingenTableView;

import java.util.List;

public interface PakketRepository {

    List<Pakket> getAllePakketten();
    List<Pakket> getPakketByKlantName(String naam);
    List<DataVoorAbonnementenTableView> getDataForAbonnementenTableViewByKlantName(String naam);
    List<DataVoorAfhalingenTableView> getDataForAfhalingenTableViewByKlantName(String naam);
    List<String> getPakkettenListByBoerNameAndKlantName(String boerNaam, String klantNaam);
}
