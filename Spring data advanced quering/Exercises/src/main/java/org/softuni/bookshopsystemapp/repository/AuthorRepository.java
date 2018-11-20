package org.softuni.bookshopsystemapp.repository;


import org.softuni.bookshopsystemapp.domain.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    //Task06
    List<Author> findAllByFirstNameEndingWith(String pattern);

    //Task08
    List<Author> findAuthorsByLastNameIsStartingWith(String letter);
    //Task10





}
