package be.kuleuven.csa.domain;

public class HaaltAf {
    private int haaltAf_id;
    private int auteur_id;
    private int verkoopt_id;
    private int pakket_weeknr;
    private int pakket_afgehaald;

    public HaaltAf() {
    }

    public HaaltAf(int auteur_id, int verkoopt_id, int pakket_weeknr, int pakket_afgehaald) {
        this.auteur_id = auteur_id;
        this.verkoopt_id = verkoopt_id;
        this.pakket_weeknr = pakket_weeknr;
        this.pakket_afgehaald = pakket_afgehaald;
    }

    public HaaltAf(int haalfAf_id, int auteur_id, int verkoopt_id, int pakket_weeknr, int pakket_afgehaald) {
        this.haaltAf_id = haalfAf_id;
        this.auteur_id = auteur_id;
        this.verkoopt_id = verkoopt_id;
        this.pakket_weeknr = pakket_weeknr;
        this.pakket_afgehaald = pakket_afgehaald;
    }

    public int getHaaltAf_id() {
        return haaltAf_id;
    }

    public void setHaaltAf_id(int haaltAf_id) {
        this.haaltAf_id = haaltAf_id;
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
                "haaltAf_id=" + haaltAf_id +
                ", auteur_id=" + auteur_id +
                ", verkoopt_id=" + verkoopt_id +
                ", pakket_weeknr=" + pakket_weeknr +
                ", pakket_afgehaald=" + pakket_afgehaald +
                '}';
    }
}
