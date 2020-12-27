package be.kuleuven.csa.domain;

public class Auteur {
    private int auteur_id;
    private String auteur_naam;

    public Auteur() {
    }

    public Auteur(int auteur_id, String auteur_naam) {
        this.auteur_id = auteur_id;
        this.auteur_naam = auteur_naam;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    public String getAuteur_naam() {
        return auteur_naam;
    }

    public void setAuteur_naam(String auteur_naam) {
        this.auteur_naam = auteur_naam;
    }

    @Override
    public String toString() {
        return "Auteur{" +
                "auteur_id=" + auteur_id +
                ", auteur_naam='" + auteur_naam + '\'' +
                '}';
    }
}
