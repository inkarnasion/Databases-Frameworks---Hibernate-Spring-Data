package org.softuni.bookshopsystemapp.domain.dtos;



import org.softuni.bookshopsystemapp.domain.entities.AgeRestriction;
import org.softuni.bookshopsystemapp.domain.entities.EditionType;

import java.math.BigDecimal;

public class BookDTO {
    private String title;
    private EditionType editionType;
    private AgeRestriction ageRestriction;
    private BigDecimal price;

    public BookDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EditionType getEditionType() {
        return editionType;
    }

    public void setEditionType(EditionType editionType) {
        this.editionType = editionType;
    }

    public AgeRestriction getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(AgeRestriction ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %.2f", title, editionType, ageRestriction, price);
    }
}
