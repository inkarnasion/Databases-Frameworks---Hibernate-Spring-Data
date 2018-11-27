package productshop.domain.dtos;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserSoldProductsDto {

    private Integer id;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private List<SoldProductsDto> soldProducts;

    public UserSoldProductsDto() {
        this.soldProducts = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<SoldProductsDto> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(List<SoldProductsDto> soldProducts) {
        this.soldProducts = soldProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSoldProductsDto that = (UserSoldProductsDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
