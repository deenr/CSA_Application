package be.kuleuven.csa.domain.helpdomain;

public class WijzigHaaltAf {
    int nieuweVerkoop_id;
    int auteur_id;
    int oudeVerkoop_id;

    public WijzigHaaltAf() {
    }

    public WijzigHaaltAf(int nieuweVerkoop_id, int auteur_id, int oudeVerkoop_id) {
        this.nieuweVerkoop_id = nieuweVerkoop_id;
        this.auteur_id = auteur_id;
        this.oudeVerkoop_id = oudeVerkoop_id;
    }

    public int getNieuweVerkoop_id() {
        return nieuweVerkoop_id;
    }

    public void setNieuweVerkoop_id(int nieuweVerkoop_id) {
        this.nieuweVerkoop_id = nieuweVerkoop_id;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    public int getOudeVerkoop_id() {
        return oudeVerkoop_id;
    }

    public void setOudeVerkoop_id(int oudeVerkoop_id) {
        this.oudeVerkoop_id = oudeVerkoop_id;
    }

    @Override
    public String toString() {
        return "WijzigPakket{" +
                "nieuweVerkoop_id=" + nieuweVerkoop_id +
                ", auteur_id=" + auteur_id +
                ", oudeVerkoop_id=" + oudeVerkoop_id +
                '}';
    }
}
