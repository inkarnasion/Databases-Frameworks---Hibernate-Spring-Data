package productshop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import productshop.domain.entities.Product;
import productshop.domain.entities.User;


import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByPriceBetweenAndBuyerOrderByPrice(BigDecimal more, BigDecimal less, User buyer);

    @Query(value = "SELECT p from productshop.domain.entities.Product AS p WHERE p.seller Is NOT NULL AND p.buyer Is NOT NULL ORDER BY p.seller.lastName ASC ,p.seller.firstName")

    List<Product> findAllWithBuyerIsNotNullAndSellerIsNotNull();
}
