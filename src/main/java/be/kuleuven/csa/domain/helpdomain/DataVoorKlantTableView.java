package be.kuleuven.csa.domain.helpdomain;

public class DataVoorKlantTableView {
    private String auteur_naam;
    private int klant_teBetalenBedrag;

    public DataVoorKlantTableView() {
    }

    public DataVoorKlantTableView(String auteur_naam, int klant_teBetalenBedrag) {
        this.auteur_naam = auteur_naam;
        this.klant_teBetalenBedrag = klant_teBetalenBedrag;
    }

    public String getAuteur_naam() {
        return auteur_naam;
    }

    public void setAuteur_naam(String auteur_naam) {
        this.auteur_naam = auteur_naam;
    }

    public int getKlant_teBetalenBedrag() {
        return klant_teBetalenBedrag;
    }

    public void setKlant_teBetalenBedrag(int klant_teBetalenBedrag) {
        this.klant_teBetalenBedrag = klant_teBetalenBedrag;
    }

    @Override
    public String toString() {
        return "DataVoorKlantTableView{" +
                "auteur_naam='" + auteur_naam + '\'' +
                ", klant_teBetalenBedrag='" + klant_teBetalenBedrag + '\'' +
                '}';
    }
}
