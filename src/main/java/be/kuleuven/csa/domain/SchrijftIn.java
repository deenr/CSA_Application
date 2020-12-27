package be.kuleuven.csa.domain;

public class SchrijftIn {
    private int schrijftIn_id;
    private int auteur_id;
    private int verkoopt_id;

    public SchrijftIn() {
    }

    public SchrijftIn(int auteur_id, int verkoopt_id) {
        this.auteur_id = auteur_id;
        this.verkoopt_id = verkoopt_id;
    }

    public SchrijftIn(int schrijftIn_id, int auteur_id, int verkoopt_id) {
        this.schrijftIn_id = schrijftIn_id;
        this.auteur_id = auteur_id;
        this.verkoopt_id = verkoopt_id;
    }

    public int getSchrijftIn_id() {
        return schrijftIn_id;
    }

    public void setSchrijftIn_id(int schrijftIn_id) {
        this.schrijftIn_id = schrijftIn_id;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
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
                "auteur_id=" + auteur_id +
                ", verkoopt_id=" + verkoopt_id +
                '}';
    }
}
