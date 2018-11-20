package org.softuni.bookshopsystemapp.repository;


import org.softuni.bookshopsystemapp.domain.entities.AgeRestriction;
import org.softuni.bookshopsystemapp.domain.entities.Author;
import org.softuni.bookshopsystemapp.domain.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByReleaseDateAfter(LocalDate date);


    //Task01
    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    //Task02
    List<Book> findAllByCopiesLessThan(Integer copies);

    //Task03
    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lp, BigDecimal gp);

    //Task04
    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate before, LocalDate after);

    //Task05
    List<Book> findAllByReleaseDateBefore(LocalDate date);

    //Task07
    List<Book> findAllByTitleContainingIgnoreCase(String text);

    //Task08
    List<Book> findAllByAuthor(Author author);

    //Task09 TODO
    //  List<Book> findAllByTitleGreaterThan(int n);

    //Task10 TODO
//    @Query(value = "SELECT a.firstName,a.lastName,sum(b.copies)" +
//            "FROM bookshopsystemapp.domain.entities.Book as b JOIN bookshopsystemapp.domain.entities.Author As a ON b.author= a GROUP BY a.firstName, a.lastName ORDER BY sum(b.copies) DESC")
//    List<Author> findTotalBooksCopies();

    //Task11
    Book findBookByTitle(String title);

}
