package org.softuni.bookshopsystemapp.domain.dtos;

public class AuthorDTO {

    private String firstName;
    private String lastName;
    private int copiesCount;

    public AuthorDTO() {
    }

    public AuthorDTO(String firstName, String lastName, int copiesCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.copiesCount = copiesCount;
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

    public int getCopiesCount() {
        return copiesCount;
    }

    public void setCopiesCount(int copiesCount) {
        this.copiesCount = copiesCount;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %d", firstName, lastName, copiesCount
        );
    }
}
