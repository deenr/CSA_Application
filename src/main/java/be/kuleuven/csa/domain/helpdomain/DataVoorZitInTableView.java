package be.kuleuven.csa.domain.helpdomain;

public class DataVoorZitInTableView {
    private String product_naam;
    private int zitIn_hoeveelheid;

    public DataVoorZitInTableView() {
    }

    public DataVoorZitInTableView(String product_naam, int zitIn_hoeveelheid) {
        this.product_naam = product_naam;
        this.zitIn_hoeveelheid = zitIn_hoeveelheid;
    }

    public String getProduct_naam() {
        return product_naam;
    }

    public void setProduct_naam(String product_naam) {
        this.product_naam = product_naam;
    }

    public int getZitIn_hoeveelheid() {
        return zitIn_hoeveelheid;
    }

    public void setZitIn_hoeveelheid(int zitIn_hoeveelheid) {
        this.zitIn_hoeveelheid = zitIn_hoeveelheid;
    }

    @Override
    public String toString() {
        return "DataVoorZitInTableView{" +
                "product_naam='" + product_naam + '\'' +
                ", zitIn_hoeveelheid=" + zitIn_hoeveelheid +
                '}';
    }
}
