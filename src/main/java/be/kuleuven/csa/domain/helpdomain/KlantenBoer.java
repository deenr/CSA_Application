package be.kuleuven.csa.domain.helpdomain;

import java.util.List;

public class KlantenBoer {
    String naam;
    List<String> pakketten;
    int prijs;

    public KlantenBoer() {
    }

    public KlantenBoer(String naam, List<String> pakketten, int prijs) {
        this.naam = naam;
        this.pakketten = pakketten;
        this.prijs = prijs;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public List<String> getPakketten() {
        return pakketten;
    }

    public void setPakketten(List<String> pakketten) {
        this.pakketten = pakketten;
    }

    public int getPrijs() {
        return prijs;
    }

    public void setPrijs(int prijs) {
        this.prijs = prijs;
    }

    @Override
    public String toString() {
        return "KlantenBoer{" +
                "naam='" + naam + '\'' +
                ", pakketten=" + pakketten +
                ", prijs=" + prijs +
                '}';
    }
}
