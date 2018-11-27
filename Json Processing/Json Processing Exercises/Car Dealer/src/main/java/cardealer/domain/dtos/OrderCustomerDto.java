package cardealer.domain.dtos;

import com.google.gson.annotations.Expose;
import cardealer.domain.entities.Sale;

import java.time.LocalDateTime;
import java.util.List;

public class OrderCustomerDto {
    @Expose
    private Integer id;
    @Expose
    private String name;
    @Expose
    private String birthDate;
    @Expose
    private boolean isYoungDriver;
    @Expose
    private List<String> sales;

    public OrderCustomerDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isYoungDriver() {
        return isYoungDriver;
    }

    public void setYoungDriver(boolean youngDriver) {
        isYoungDriver = youngDriver;
    }

    public List<String> getSales() {
        return sales;
    }

    public void setSales(List<String> sales) {
        this.sales = sales;
    }
}
