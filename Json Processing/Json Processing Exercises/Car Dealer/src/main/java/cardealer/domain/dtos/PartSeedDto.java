package cardealer.domain.dtos;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class PartSeedDto {
    @Expose
    private String name;
    @Expose
    private BigDecimal price;
    @Expose
    private double quantity;

    public PartSeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "PartSeedDto{" + "name='" + name + '\'' + ", price=" + price + ", quantity=" + quantity + '}';
    }
}
