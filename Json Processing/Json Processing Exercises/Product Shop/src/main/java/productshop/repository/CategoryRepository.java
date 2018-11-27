package productshop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productshop.domain.entities.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    //SELECT c.name,count(cp.product_id) AS product_number,AVG(p.price)As avr_price,Sum(p.price)AS total_price_sum
//    from category_products cp
//    JOIN categories c on cp.category_id = c.id
//    JOIN products p on cp.product_id = p.id
//    GROUP BY cp.category_id
//    ORDER BY product_number DESC;
//    @Query(value = "SELECT c.name,size(c.products),avg(p.price),sum(p.price) from Category as c JOIN c.products as p GROUP BY c.id order by size(c.products)DESC ")
//    List<CategoriesByProductsDto> findCategoriesByProducts();


}
