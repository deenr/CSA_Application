package be.kuleuven.csa.domain;

import java.nio.charset.Charset;
import java.util.Random;

public class Tip {
    private String _id;
    private int product_id;
    private String auteur_naam;
    private String tip_file;

    public Tip() {
    }

    public Tip(int product_id, String auteur_naam, String tip_file) {
        this._id = product_id + tip_file;
        this.product_id = product_id;
        this.auteur_naam = auteur_naam;
        this.tip_file = tip_file;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getAuteur_naam() {
        return auteur_naam;
    }

    public void setAuteur_naam(String auteur_naam) {
        this.auteur_naam = auteur_naam;
    }

    public String getTip_file() {
        return tip_file;
    }

    public void setTip_file(String tip_file) {
        this.tip_file = tip_file;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Tip{" +
                "product_id=" + product_id +
                ", auteur_naam=" + auteur_naam +
                ", tip_file='" + tip_file + '\'' +
                '}';
    }
}
