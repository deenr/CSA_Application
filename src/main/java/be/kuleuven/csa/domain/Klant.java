package be.kuleuven.csa.domain;

public class Klant {
    private int auteur_id;
    private int klant_teBetalenBedrag;

    public Klant() {
    }

    public Klant(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    public Klant(int auteur_id, int klant_teBetalenBedrag) {
        this.auteur_id = auteur_id;
        this.klant_teBetalenBedrag = klant_teBetalenBedrag;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    public int getKlant_teBetalenBedrag() {
        return klant_teBetalenBedrag;
    }

    public void setKlant_teBetalenBedrag(int klant_teBetalenBedrag) {
        this.klant_teBetalenBedrag = klant_teBetalenBedrag;
    }

    @Override
    public String toString() {
        return "Klant{" +
                "auteur_id=" + auteur_id +
                ", klant_teBetalenBedrag=" + klant_teBetalenBedrag +
                '}';
    }
}
