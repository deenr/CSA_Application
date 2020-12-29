package be.kuleuven.csa.domain;

import be.kuleuven.csa.domain.helpdomain.DataVoorAbonnementenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorAfhalingenTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorPakketTableView;
import be.kuleuven.csa.domain.helpdomain.DataVoorVeranderdStatusPakket;

import java.util.List;

public interface PakketRepository {

    List<Pakket> getAllePakketten();
    List<DataVoorPakketTableView> getAllePakkettenVoorDataView();
    List<Pakket> getPakketByKlantName(String naam);
    List<DataVoorAbonnementenTableView> getDataForAbonnementenTableViewByKlantName(String naam);
    List<DataVoorAfhalingenTableView> getDataForAfhalingenTableViewByKlantName(String naam);
    List<String> getPakkettenListByBoerNameAndKlantName(String boerNaam, String klantNaam);
    List<DataVoorVeranderdStatusPakket> getDataForVeranderingStatusTableViewByBoerName(String naam);
}
