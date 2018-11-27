package productshop.domain.dtos;

import com.google.gson.annotations.Expose;

public class CategoriesByProductsDto {
    @Expose
    private String name;
    @Expose
    private int productsNumber;
    @Expose
    private double averageProductsPrice;
    @Expose
    private double totalPriceSum;

    public CategoriesByProductsDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductsNumber() {
        return productsNumber;
    }

    public void setProductsNumber(int productsNumber) {
        this.productsNumber = productsNumber;
    }

    public double getAverageProductsPrice() {
        return averageProductsPrice;
    }

    public void setAverageProductsPrice(double averageProductsPrice) {
        this.averageProductsPrice = averageProductsPrice;
    }

    public double getTotalPriceSum() {
        return totalPriceSum;
    }

    public void setTotalPriceSum(double totalPriceSum) {
        this.totalPriceSum = totalPriceSum;
    }
}
