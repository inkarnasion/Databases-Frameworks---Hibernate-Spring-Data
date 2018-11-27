package cardealer.domain.entities;

import javax.persistence.*;

@MappedSuperclass
public class Base {

    private Integer id;

    public Base() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
