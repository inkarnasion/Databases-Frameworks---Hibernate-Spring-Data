package productshop.domain.dtos;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductsDto {
    @Expose
    private int count;

    @Expose
    private List<ProductDto> products;

    public ProductsDto() {
        this.products = new ArrayList<>();
    }

    public void addProduct(ProductDto product){
        products.add(product);
        count = products.size();
    }
    public int getProductsCount() {
        return products.size();
    }
}
