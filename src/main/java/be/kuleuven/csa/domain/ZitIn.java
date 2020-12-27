package be.kuleuven.csa.domain;

public class ZitIn {
    private int product_id;
    private int verkoopt_id;
    private int zitIn_hoeveelheid;
    private int zitIn_weeknr;

    public ZitIn() {
    }

    public ZitIn(int product_id, int verkoopt_id, int zitIn_hoeveelheid, int zitIn_weeknr) {
        this.product_id = product_id;
        this.verkoopt_id = verkoopt_id;
        this.zitIn_hoeveelheid = zitIn_hoeveelheid;
        this.zitIn_weeknr = zitIn_weeknr;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getVerkoopt_id() {
        return verkoopt_id;
    }

    public void setVerkoopt_id(int verkoopt_id) {
        this.verkoopt_id = verkoopt_id;
    }

    public int getZitIn_hoeveelheid() {
        return zitIn_hoeveelheid;
    }

    public void setZitIn_hoeveelheid(int zitIn_hoeveelheid) {
        this.zitIn_hoeveelheid = zitIn_hoeveelheid;
    }

    public int getZitIn_weeknr() {
        return zitIn_weeknr;
    }

    public void setZitIn_weeknr(int zitIn_weeknr) {
        this.zitIn_weeknr = zitIn_weeknr;
    }

    @Override
    public String toString() {
        return "ZitIn{" +
                "product_id=" + product_id +
                ", verkoopt_id=" + verkoopt_id +
                ", zitIn_hoeveelheid=" + zitIn_hoeveelheid +
                ", zitIn_weeknr=" + zitIn_weeknr +
                '}';
    }
}
