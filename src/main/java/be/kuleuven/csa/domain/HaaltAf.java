package be.kuleuven.csa.domain;

public class HaaltAf {
    private int haalfAf_id;
    private int auteur_id;
    private int verkoopt_id;
    private int pakket_weeknr;
    private int pakket_afgehaald;

    public HaaltAf() {
    }

    public HaaltAf(int auteur_id, int verkoopt_id, int pakket_weeknr) {
        this.auteur_id = auteur_id;
        this.verkoopt_id = verkoopt_id;
        this.pakket_weeknr = pakket_weeknr;
        this.pakket_afgehaald = 0;
    }

    public HaaltAf(int auteur_id, int verkoopt_id, int pakket_weeknr, int pakket_afgehaald) {
        this.auteur_id = auteur_id;
        this.verkoopt_id = verkoopt_id;
        this.pakket_weeknr = pakket_weeknr;
        this.pakket_afgehaald = pakket_afgehaald;
    }

    public HaaltAf(int haalfAf_id, int auteur_id, int verkoopt_id, int pakket_weeknr, int pakket_afgehaald) {
        this.haalfAf_id = haalfAf_id;
        this.auteur_id = auteur_id;
        this.verkoopt_id = verkoopt_id;
        this.pakket_weeknr = pakket_weeknr;
        this.pakket_afgehaald = pakket_afgehaald;
    }

    public int getHaalfAf_id() {
        return haalfAf_id;
    }

    public void setHaalfAf_id(int haalfAf_id) {
        this.haalfAf_id = haalfAf_id;
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

    public int getPakket_weeknr() {
        return pakket_weeknr;
    }

    public void setPakket_weeknr(int pakket_weeknr) {
        this.pakket_weeknr = pakket_weeknr;
    }

    public int getPakket_afgehaald() {
        return pakket_afgehaald;
    }

    public void setPakket_afgehaald(int pakket_afgehaald) {
        this.pakket_afgehaald = pakket_afgehaald;
    }

    @Override
    public String toString() {
        return "HaaltAf{" +
                "auteur_id=" + auteur_id +
                ", verkoopt_id=" + verkoopt_id +
                ", pakket_weeknr=" + pakket_weeknr +
                ", pakket_afgehaald=" + pakket_afgehaald +
                '}';
    }
}
