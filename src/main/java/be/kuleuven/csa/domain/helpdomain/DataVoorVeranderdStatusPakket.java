package be.kuleuven.csa.domain.helpdomain;

public class DataVoorVeranderdStatusPakket {
    private String auteur_naam;
    private String pakket_naam;
    private int pakket_weeknr;
    private int pakket_afgehaald;

    public DataVoorVeranderdStatusPakket() {
    }

    public DataVoorVeranderdStatusPakket(String auteur_naam, String pakket_naam, int pakket_weeknr, int pakket_afgehaald) {
        this.auteur_naam = auteur_naam;
        this.pakket_naam = pakket_naam;
        this.pakket_weeknr = pakket_weeknr;
        this.pakket_afgehaald = pakket_afgehaald;
    }

    public String getAuteur_naam() {
        return auteur_naam;
    }

    public void setAuteur_naam(String auteur_naam) {
        this.auteur_naam = auteur_naam;
    }

    public String getPakket_naam() {
        return pakket_naam;
    }

    public void setPakket_naam(String pakket_naam) {
        this.pakket_naam = pakket_naam;
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
        return "VeranderdStatusPakket{" +
                "auteur_naam='" + auteur_naam + '\'' +
                ", pakket_naam='" + pakket_naam + '\'' +
                ", pakket_weeknr=" + pakket_weeknr +
                ", pakket_afgehaald=" + pakket_afgehaald +
                '}';
    }
}
