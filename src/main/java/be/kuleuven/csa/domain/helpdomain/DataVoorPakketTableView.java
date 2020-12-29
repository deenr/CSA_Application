package be.kuleuven.csa.domain.helpdomain;

public class DataVoorPakketTableView {
    private String boer_naam;
    private String pakket_naam;
    private String klant_naam;
    private int weeknr;
    private int pakket_afgehaald;

    public DataVoorPakketTableView() {
    }

    public DataVoorPakketTableView(String boer_naam, String pakket_naam, String klant_naam, int weeknr, int pakket_afgehaald) {
        this.boer_naam = boer_naam;
        this.pakket_naam = pakket_naam;
        this.klant_naam = klant_naam;
        this.weeknr = weeknr;
        this.pakket_afgehaald = pakket_afgehaald;
    }

    public String getBoer_naam() {
        return boer_naam;
    }

    public void setBoer_naam(String boer_naam) {
        this.boer_naam = boer_naam;
    }

    public String getPakket_naam() {
        return pakket_naam;
    }

    public void setPakket_naam(String pakket_naam) {
        this.pakket_naam = pakket_naam;
    }

    public String getKlant_naam() {
        return klant_naam;
    }

    public void setKlant_naam(String klant_naam) {
        this.klant_naam = klant_naam;
    }

    public int getWeeknr() {
        return weeknr;
    }

    public void setWeeknr(int weeknr) {
        this.weeknr = weeknr;
    }

    public int getPakket_afgehaald() {
        return pakket_afgehaald;
    }

    public void setPakket_afgehaald(int pakket_afgehaald) {
        this.pakket_afgehaald = pakket_afgehaald;
    }

    @Override
    public String toString() {
        return "DataVoorPakketTableView{" +
                "boer_naam='" + boer_naam + '\'' +
                ", pakket_naam='" + pakket_naam + '\'' +
                ", klant_naam='" + klant_naam + '\'' +
                ", weeknr=" + weeknr +
                ", pakket_afgehaald=" + pakket_afgehaald +
                '}';
    }
}
