package be.kuleuven.csa.domain;

import java.util.List;

public interface BoerRepository {

    List<Boer> getAlleBoeren();
    List<Boer> getBoerByName(String naam);
    void saveNewBoer(Boer boer);
    void updateBoer(Boer boer);
    List<String> getAlleBoerNamen();
}
