package be.kuleuven.csa.domain;

import be.kuleuven.csa.domain.helpdomain.DataVoorBoerTableView;

import java.util.List;

public interface BoerRepository {

    List<DataVoorBoerTableView> getAlleBoerenVoorDataView();
    List<Boer> getBoerByName(String naam);
    void saveNewBoer(Boer boer);
    void wijzigBoer(Boer boer);
    List<String> getAlleBoerNamen();
    void verwijderBoerByAuteurID(int auteur_id);
}
