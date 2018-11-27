package productshop.domain.dtos;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class UserAgeSoldProductsDto {
    private Integer id;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    int age;
    @Expose
    private ProductsDto soldProducts;

    public UserAgeSoldProductsDto() {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ProductsDto getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(ProductsDto soldProducts) {
        this.soldProducts = soldProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAgeSoldProductsDto that = (UserAgeSoldProductsDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
