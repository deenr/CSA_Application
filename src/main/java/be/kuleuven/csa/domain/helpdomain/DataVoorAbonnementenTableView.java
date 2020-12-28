package be.kuleuven.csa.domain.helpdomain;

public class DataVoorAbonnementenTableView {
    private int pakket_id;
    private String pakket_naam;
    private String auteur_naam;
    private int verkoopt_prijs;
    private int pakket_aantalVolwassenen;
    private int pakket_aantalKinderen;

    public DataVoorAbonnementenTableView() {
    }

    public DataVoorAbonnementenTableView(int pakket_id, String pakket_naam, String auteur_naam, int verkoopt_prijs, int pakket_aantalVolwassenen, int pakket_aantalKinderen) {
        this.pakket_id = pakket_id;
        this.pakket_naam = pakket_naam;
        this.auteur_naam = auteur_naam;
        this.verkoopt_prijs = verkoopt_prijs;
        this.pakket_aantalVolwassenen = pakket_aantalVolwassenen;
        this.pakket_aantalKinderen = pakket_aantalKinderen;
    }

    public int getPakket_id() {
        return pakket_id;
    }

    public void setPakket_id(int pakket_id) {
        this.pakket_id = pakket_id;
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

    public int getPakket_aantalVolwassenen() {
        return pakket_aantalVolwassenen;
    }

    public void setPakket_aantalVolwassenen(int pakket_aantalVolwassenen) {
        this.pakket_aantalVolwassenen = pakket_aantalVolwassenen;
    }

    public int getPakket_aantalKinderen() {
        return pakket_aantalKinderen;
    }

    public void setPakket_aantalKinderen(int pakket_aantalKinderen) {
        this.pakket_aantalKinderen = pakket_aantalKinderen;
    }

    public int getVerkoopt_prijs() {
        return verkoopt_prijs;
    }

    public void setVerkoopt_prijs(int verkoopt_prijs) {
        this.verkoopt_prijs = verkoopt_prijs;
    }

    @Override
    public String toString() {
        return "DataVoorKlantTableView{" +
                "pakket_id=" + pakket_id +
                ", pakket_naam='" + pakket_naam + '\'' +
                ", auteur_naam='" + auteur_naam + '\'' +
                ", verkoopt_prijs=" + verkoopt_prijs +
                ", pakket_aantalVolwassenen=" + pakket_aantalVolwassenen +
                ", pakket_aantalKinderen=" + pakket_aantalKinderen +
                '}';
    }
}

