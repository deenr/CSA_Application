package be.kuleuven.csa.domain;

public class HaaltAf {
    private int klant_id;
    private int verkoopt_id;
    private int pakket_weeknr;
    private int pakket_afgehaald;

    public HaaltAf() {
    }

    public HaaltAf(int klant_id, int verkoopt_id, int pakket_weeknr) {
        this.klant_id = klant_id;
        this.verkoopt_id = verkoopt_id;
        this.pakket_weeknr = pakket_weeknr;
        this.pakket_afgehaald = 0;
    }

    public HaaltAf(int klant_id, int verkoopt_id, int pakket_weeknr, int pakket_afgehaald) {
        this.klant_id = klant_id;
        this.verkoopt_id = verkoopt_id;
        this.pakket_weeknr = pakket_weeknr;
        this.pakket_afgehaald = pakket_afgehaald;
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
                "klant_id=" + klant_id +
                ", verkoopt_id=" + verkoopt_id +
                ", pakket_weeknr=" + pakket_weeknr +
                ", pakket_afgehaald=" + pakket_afgehaald +
                '}';
    }
}
