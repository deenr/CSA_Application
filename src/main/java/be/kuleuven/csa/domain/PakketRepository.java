package be.kuleuven.csa.domain;

import be.kuleuven.csa.domain.helpdomain.DataVoorKlantTableView;

import java.util.List;

public interface PakketRepository {

    List<Pakket> getAllePakketten();
    List<Pakket> getPakketByKlantName(String naam);
    List<DataVoorKlantTableView> getDataForKlantTableViewByKlantName(String naam);
}
