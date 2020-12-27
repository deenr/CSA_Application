package be.kuleuven.csa.domain;

import java.util.List;

public interface VerkooptRepository {

    List<Verkoopt> getVerkooptByBoerAndSize(int auteur_id, int pakket_id);
}
