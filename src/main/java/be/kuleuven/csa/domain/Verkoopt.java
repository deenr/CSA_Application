package be.kuleuven.csa.domain;

public class Verkoopt {
    private int verkoopt_id;
    private int auteur_id;
    private int pakket_id;
    private int verkoopt_prijs;

    public Verkoopt() {
    }

    public Verkoopt(int auteur_id, int pakket_id, int verkoopt_prijs) {
        this.auteur_id = auteur_id;
        this.pakket_id = pakket_id;
        this.verkoopt_prijs = verkoopt_prijs;
    }

    public int getVerkoopt_id() {
        return verkoopt_id;
    }

    public void setVerkoopt_id(int verkoopt_id) {
        this.verkoopt_id = verkoopt_id;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    public int getPakket_id() {
        return pakket_id;
    }

    public void setPakket_id(int pakket_id) {
        this.pakket_id = pakket_id;
    }

    public int getVerkoopt_prijs() {
        return verkoopt_prijs;
    }

    public void setVerkoopt_prijs(int verkoopt_prijs) {
        this.verkoopt_prijs = verkoopt_prijs;
    }

    @Override
    public String toString() {
        return "Verkoopt{" +
                "verkoopt_id=" + verkoopt_id +
                ", auteur_id=" + auteur_id +
                ", pakket_id=" + pakket_id +
                ", verkoopt_prijs=" + verkoopt_prijs +
                '}';
    }
}
