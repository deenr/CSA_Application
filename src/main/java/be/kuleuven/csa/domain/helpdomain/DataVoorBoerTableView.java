package be.kuleuven.csa.domain.helpdomain;

public class DataVoorBoerTableView {
    private String auteur_naam;
    private String boer_adres;

    public DataVoorBoerTableView() {
    }

    public DataVoorBoerTableView(String auteur_naam, String boer_adres) {
        this.auteur_naam = auteur_naam;
        this.boer_adres = boer_adres;
    }

    public String getAuteur_naam() {
        return auteur_naam;
    }

    public void setAuteur_naam(String auteur_naam) {
        this.auteur_naam = auteur_naam;
    }

    public String getBoer_adres() {
        return boer_adres;
    }

    public void setBoer_adres(String boer_adres) {
        this.boer_adres = boer_adres;
    }

    @Override
    public String toString() {
        return "DataVoorBoerTableView{" +
                "auteur_naam='" + auteur_naam + '\'' +
                ", boer_adres='" + boer_adres + '\'' +
                '}';
    }
}
