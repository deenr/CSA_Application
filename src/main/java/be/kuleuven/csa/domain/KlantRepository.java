package be.kuleuven.csa.domain;

import be.kuleuven.csa.domain.helpdomain.DataVoorKlantTableView;

import java.util.ArrayList;
import java.util.List;

public interface KlantRepository {

    List<DataVoorKlantTableView> getAlleKlantenVoorDataView();
    List<Klant> getKlantByName(String naam);
    List<Klant> getKlantByVerkooptID(int id);
    List<String> getKlantenByBoerName(String naam);
    void saveNewKlant(Klant klant);
    void updateKlant(Klant klant);
}
