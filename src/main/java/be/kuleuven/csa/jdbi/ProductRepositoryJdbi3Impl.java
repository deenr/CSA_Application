package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.Klant;
import be.kuleuven.csa.domain.Product;
import be.kuleuven.csa.domain.ProductRepository;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ProductRepositoryJdbi3Impl implements ProductRepository {

    private final Jdbi jdbi;

    public ProductRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public List<Product> getProductByName(String naam) {
        var query = "SELECT * FROM Product WHERE product_naam = '" + naam + "';";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Product.class)
                    .list();
        });
    }

    @Override
    public List<Product> getProductByProductID(int product_id) {
        var query = "SELECT * FROM Product WHERE product_id = '" + product_id + "';";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Product.class)
                    .list();
        });
    }

    @Override
    public void saveNewProduct(Product product) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO Product(product_naam, product_soort) VALUES (?,?);")
                    .bind(0, product.getProduct_naam())
                    .bind(1, product.getProduct_soort())
                    .execute();
        });
    }

    public List<String> getAlleProductenBySoort(String soort) {
        var query = "SELECT product_naam FROM Product WHERE product_soort = '" + soort + "';";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapTo(String.class)
                    .list();
        });
    }

    @Override
    public List<String> getAlleProducten() {
        var query = "SELECT product_naam FROM Product;";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapTo(String.class)
                    .list();
        });
    }
    @Override
    public List<Product> getAlleProductenVoorDataView() {
        var query = "SELECT * FROM Product;";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Product.class)
                    .list();
        });
    }
}
