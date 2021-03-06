package be.kuleuven.csa.domain;

import java.util.List;

public interface AuteurRepository {

    List<Auteur> getAllAuteurs();
    List<Auteur> getAuteurByName(String naam);
    List<Auteur> getAuteurByID(int id);
    void saveNewAuteur(Auteur auteur);
    void wijzigAuteur(Auteur auteur);
    void verwijderAuteurByAuteurID(int auteur_id);
}
