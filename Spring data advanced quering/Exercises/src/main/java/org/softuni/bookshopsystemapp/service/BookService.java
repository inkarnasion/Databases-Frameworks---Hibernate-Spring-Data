package org.softuni.bookshopsystemapp.service;



import org.softuni.bookshopsystemapp.domain.dtos.AuthorDTO;
import org.softuni.bookshopsystemapp.domain.dtos.BookDTO;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface BookService {

    void seedBooks() throws IOException;

    List<String> getAllBooksTitlesAfter();

    Set<String> getAllAuthorsWithBookBefore();

    //Task01
    List<String> getBooksByAgeRestiction(String input);

    //Task02
    List<String> getGoldenBooks();

    //Task03
    List<String> getBooksInPriceRange();

    //Task04
    String notReleasedBooks(String yearAsString);

    //Task05
    List<String> getBooksWithReleasedDateBefore(String date);

    //Task07
    List<String> getBooksByTitlePattern(String s);

    //Task08
    List<String> getBooksByAuthorWithLastNameStartWith(String s);

    //Task09
    int getBooksCount(int input);

    //Task10
    List<AuthorDTO> getTotalBooksCopies();

    //Task11
    BookDTO getBooksByTitle(String text);
}
