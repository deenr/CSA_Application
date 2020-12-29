package be.kuleuven.csa.domain.helpdomain;

public class DataVoorTipTableView {
    private String auteur_naam;
    private String product_naam;
    private String product_soort;
    private String tip_url;

    public DataVoorTipTableView() {
    }

    public DataVoorTipTableView(String auteur_naam, String product_naam, String product_soort, String tip_url) {
        this.auteur_naam = auteur_naam;
        this.product_naam = product_naam;
        this.product_soort = product_soort;
        this.tip_url = tip_url;
    }

    public String getAuteur_naam() {
        return auteur_naam;
    }

    public void setAuteur_naam(String auteur_naam) {
        this.auteur_naam = auteur_naam;
    }

    public String getProduct_naam() {
        return product_naam;
    }

    public void setProduct_naam(String product_naam) {
        this.product_naam = product_naam;
    }

    public String getProduct_soort() {
        return product_soort;
    }

    public void setProduct_soort(String product_soort) {
        this.product_soort = product_soort;
    }

    public String getTip_url() {
        return tip_url;
    }

    public void setTip_url(String tip_url) {
        this.tip_url = tip_url;
    }

    @Override
    public String toString() {
        return "Tip{" +
                "auteur_naam='" + auteur_naam + '\'' +
                ", product_naam='" + product_naam + '\'' +
                ", product_soort='" + product_soort + '\'' +
                ", tip_url='" + tip_url + '\'' +
                '}';
    }
}
