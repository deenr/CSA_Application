package be.kuleuven.csa.domain;

public class SchrijftIn {
    private int klant_id;
    private int verkoopt_id;

    public SchrijftIn() {
    }

    public SchrijftIn(int klant_id, int verkoopt_id) {
        this.klant_id = klant_id;
        this.verkoopt_id = verkoopt_id;
    }

    public int getKlant_id() {
        return klant_id;
    }

    public void setKlant_id(int klant_id) {
        this.klant_id = klant_id;
    }

    public int getVerkoopt_id() {
        return verkoopt_id;
    }

    public void setVerkoopt_id(int verkoopt_id) {
        this.verkoopt_id = verkoopt_id;
    }

    @Override
    public String toString() {
        return "SchrijftIn{" +
                "klant_id=" + klant_id +
                ", verkoopt_id=" + verkoopt_id +
                '}';
    }
}
