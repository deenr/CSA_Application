package be.kuleuven.csa.domain;

import java.util.List;

public interface ProductRepository {

    List<Product> getProductByName(String naam);
    List<Product> getProductByProductID(int product_id);
    void saveNewProduct(Product product);
    List<String> getAlleProductenBySoort(String soort);
    List<String> getAlleProducten();
    List<Product> getAlleProductenVoorDataView();
}
