package be.kuleuven.csa.domain;

public class Product {
    private int product_id;
    private String product_naam;
    private String product_soort;

    public Product() {
    }

    public Product(int product_id, String product_naam) {
        this.product_id = product_id;
        this.product_naam = product_naam;
        this.product_soort = null;
    }

    public Product(int product_id, String product_naam, String product_soort) {
        this.product_id = product_id;
        this.product_naam = product_naam;
        this.product_soort = product_soort;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", product_naam='" + product_naam + '\'' +
                ", product_soort='" + product_soort + '\'' +
                '}';
    }
}
