package be.kuleuven.csa.domain;

import java.util.List;

public interface ProductRepository {

    List<Product> getProductByName(String naam);
    void saveNewProduct(Product product);
}
