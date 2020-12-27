package be.kuleuven.csa.domain.helpdomain;

public class PakketBoerVoorTable {
    private int pakket_id;
    private String pakket_naam;
    private String auteur_naam;
    private int pakket_aantalVolwassenen;
    private int Pakket_aantalKinderen;

    public PakketBoerVoorTable() {
    }

    public PakketBoerVoorTable(int pakket_id, String pakket_naam, String auteur_naam, int pakket_aantalVolwassenen, int pakket_aantalKinderen) {
        this.pakket_id = pakket_id;
        this.pakket_naam = pakket_naam;
        this.auteur_naam = auteur_naam;
        this.pakket_aantalVolwassenen = pakket_aantalVolwassenen;
        Pakket_aantalKinderen = pakket_aantalKinderen;
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
        return Pakket_aantalKinderen;
    }

    public void setPakket_aantalKinderen(int pakket_aantalKinderen) {
        Pakket_aantalKinderen = pakket_aantalKinderen;
    }

    @Override
    public String toString() {
        return "PakketBoerVoorTable{" +
                "pakket_id=" + pakket_id +
                ", pakket_naam='" + pakket_naam + '\'' +
                ", auteur_naam='" + auteur_naam + '\'' +
                ", pakket_aantalVolwassenen=" + pakket_aantalVolwassenen +
                ", Pakket_aantalKinderen=" + Pakket_aantalKinderen +
                '}';
    }
}

