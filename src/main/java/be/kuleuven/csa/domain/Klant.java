package be.kuleuven.csa.domain;

public class Klant {
    private int auteur_id;

    public Klant() {
    }

    public Klant(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    @Override
    public String toString() {
        return "Klant{" +
                "auteur_id=" + auteur_id +
                '}';
    }
}
