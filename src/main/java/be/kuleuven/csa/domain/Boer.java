package be.kuleuven.csa.domain;

public class Boer {
    private int auteur_id;
    private String boer_adres;

    public Boer() {
    }

    public Boer(int auteur_id, String boer_adres) {
        this.auteur_id = auteur_id;
        this.boer_adres = boer_adres;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    public String getBoer_adres() {
        return boer_adres;
    }

    public void setBoer_adres(String boer_adres) {
        this.boer_adres = boer_adres;
    }

    @Override
    public String toString() {
        return "Boer{" +
                "auteur_id=" + auteur_id +
                ", boer_adres='" + boer_adres + '\'' +
                '}';
    }
}
