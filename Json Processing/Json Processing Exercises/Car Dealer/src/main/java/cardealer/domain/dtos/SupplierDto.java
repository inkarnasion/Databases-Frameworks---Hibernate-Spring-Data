package cardealer.domain.dtos;

import com.google.gson.annotations.Expose;
import cardealer.domain.entities.Part;

import java.util.List;

public class SupplierDto {
    @Expose
    private Integer id;
    @Expose
    private String name;
    @Expose
    private int partsCount;

    public SupplierDto() {
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

    public int getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(int partsCount) {
        this.partsCount = partsCount;
    }
}
