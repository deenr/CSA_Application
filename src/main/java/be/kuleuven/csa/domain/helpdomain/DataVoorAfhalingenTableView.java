package be.kuleuven.csa.domain.helpdomain;

public class DataVoorAfhalingenTableView {
    private String pakket_naam;
    private String auteur_naam;
    private int pakket_weeknr;
    private String boer_adres;

    public DataVoorAfhalingenTableView() {
    }

    public DataVoorAfhalingenTableView(String pakket_naam, String auteur_naam, int pakket_weeknr, String boer_adres) {
        this.pakket_naam = pakket_naam;
        this.auteur_naam = auteur_naam;
        this.pakket_weeknr = pakket_weeknr;
        this.boer_adres = boer_adres;
    }

    public String getPakket_naam() {
        return pakket_naam;
    }

    public void setPakket_naam(String pakket_naam) {
        this.pakket_naam = pakket_naam;
    }

    public String getAuteur_naam() {
        return auteur_naam;
    }

    public void setAuteur_naam(String auteur_naam) {
        this.auteur_naam = auteur_naam;
    }

    public int getPakket_weeknr() {
        return pakket_weeknr;
    }

    public void setPakket_weeknr(int pakket_weeknr) {
        this.pakket_weeknr = pakket_weeknr;
    }

    public String getBoer_adres() {
        return boer_adres;
    }

    public void setBoer_adres(String boer_adres) {
        this.boer_adres = boer_adres;
    }

    @Override
    public String toString() {
        return "DataVoorAfhalingenTableView{" +
                "pakket_naam='" + pakket_naam + '\'' +
                ", auteur_naam='" + auteur_naam + '\'' +
                ", pakket_weeknr=" + pakket_weeknr +
                ", boer_adres='" + boer_adres + '\'' +
                '}';
    }
}
