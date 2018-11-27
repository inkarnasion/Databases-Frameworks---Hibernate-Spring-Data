package cardealer.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity(name = "cars")
public class Car extends Base {
    private String make;
    private String model;
    private long travelledDistance;

    private List<Part> parts;

    public Car() {
    }

    @Column(name = "make")
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Column(name = "model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "distance_travel")
    public long getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(long travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    @ManyToMany(targetEntity = Part.class)
    @JoinTable(name = "parts_cars",
               joinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "car_id", referencedColumnName = "id"))

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
