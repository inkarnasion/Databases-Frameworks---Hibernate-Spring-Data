package org.softuni.bookshopsystemapp.service;


import org.modelmapper.ModelMapper;
import org.softuni.bookshopsystemapp.domain.dtos.AuthorDTO;
import org.softuni.bookshopsystemapp.domain.dtos.BookDTO;
import org.softuni.bookshopsystemapp.domain.entities.*;
import org.softuni.bookshopsystemapp.repository.AuthorRepository;
import org.softuni.bookshopsystemapp.repository.BookRepository;
import org.softuni.bookshopsystemapp.repository.CategoryRepository;
import org.softuni.bookshopsystemapp.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final static String BOOKS_FILE_PATH = "D:\\Java\\Spring Data Advanced Homeworks\\src\\main\\resources\\files\\books.txt";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;
    private ModelMapper modelMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, FileUtil fileUtil) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() != 0) {
            return;
        }

        String[] booksFileContent = this.fileUtil.getFileContent(BOOKS_FILE_PATH);
        for (String line : booksFileContent) {
            String[] lineParams = line.split("\\s+");

            Book book = new Book();
            book.setAuthor(this.getRandomAuthor());

            EditionType editionType = EditionType.values()[Integer.parseInt(lineParams[0])];
            book.setEditionType(editionType);

            LocalDate releaseDate = LocalDate.parse(lineParams[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
            book.setReleaseDate(releaseDate);

            int copies = Integer.parseInt(lineParams[2]);
            book.setCopies(copies);

            BigDecimal price = new BigDecimal(lineParams[3]);
            book.setPrice(price);

            AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(lineParams[4])];
            book.setAgeRestriction(ageRestriction);

            StringBuilder title = new StringBuilder();
            for (int i = 5; i < lineParams.length; i++) {
                title.append(lineParams[i]).append(" ");
            }

            book.setTitle(title.toString().trim());

            Set<Category> categories = this.getRandomCategories();
            book.setCategories(categories);

            this.bookRepository.saveAndFlush(book);
        }
    }

    @Override
    public List<String> getAllBooksTitlesAfter() {
        List<Book> books = this.bookRepository.findAllByReleaseDateAfter(LocalDate.parse("2000-12-31"));

        return books.stream().map(b -> b.getTitle()).collect(Collectors.toList());
    }

    @Override
    public Set<String> getAllAuthorsWithBookBefore() {
        List<Book> books = this.bookRepository.findAllByReleaseDateBefore(LocalDate.parse("1990-01-01"));

        return books.stream().map(b -> String.format("%s %s", b.getAuthor().getFirstName(), b.getAuthor().getLastName())).collect(Collectors.toSet());
    }

    //Task01
    @Override
    public List<String> getBooksByAgeRestiction(String input) {
        List<Book> books = this.bookRepository.findAllByAgeRestriction(AgeRestriction.valueOf(input.toUpperCase()));
        return books.stream().map(b -> String.format("%s", b.getTitle())).collect(Collectors.toList());
    }

    //Task02
    @Override
    public List<String> getGoldenBooks() {
        List<Book> books = this.bookRepository.findAllByCopiesLessThan(5000);
        return books.stream().map(b -> String.format("%s - %s", b.getTitle(), b.getCopies())).collect(Collectors.toList());

    }

    //Task03
    @Override
    public List<String> getBooksInPriceRange() {
        List<Book> books = this.bookRepository.findAllByPriceLessThanOrPriceGreaterThan(BigDecimal.valueOf(5), BigDecimal.valueOf(40));
        return books.stream().map(b -> String.format("%s - $%.2f", b.getTitle(), b.getPrice())).collect(Collectors.toList());

    }

    //Task04
    @Override
    public String notReleasedBooks(String yearAsString) {
        LocalDate before = LocalDate.parse(yearAsString + "-01-01");
        LocalDate after = LocalDate.parse(yearAsString + "-12-31");

        List<Book> books = this.bookRepository.findAllByReleaseDateBeforeOrReleaseDateAfter(before, after);

        List<String> bookTitles = books.stream().map(book -> book.getTitle()).collect(Collectors.toList());

        return String.join(System.lineSeparator(), bookTitles);

    }

    //Task05
    @Override
    public List<String> getBooksWithReleasedDateBefore(String inputDate) {
        LocalDate date = LocalDate
                .parse(inputDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<Book> books = this.bookRepository.findAllByReleaseDateBefore(date);

        return books.stream().map(b -> String.format("%s - %s - $%.2f", b.getTitle(), b.getEditionType().toString(), b.getPrice())).collect(Collectors.toList());
    }

    //Task07
    @Override
    public List<String> getBooksByTitlePattern(String s) {
        List<Book> books = this.bookRepository.findAllByTitleContainingIgnoreCase(s);
        return books.stream().map(b -> b.getTitle()).collect(Collectors.toList());
    }


    private Author getRandomAuthor() {
        Random random = new Random();

        int randomId = random.nextInt((int) (this.authorRepository.count() - 1)) + 1;

        return this.authorRepository.findById(randomId).orElse(null);
    }

    //Task08
    @Override
    public List<String> getBooksByAuthorWithLastNameStartWith(String s) {
        List<String> result = new ArrayList<String>();
        List<Author> authors = this.authorRepository.findAuthorsByLastNameIsStartingWith(s);

        for (Author a : authors) {
            List<Book> books = this.bookRepository.findAllByAuthor(a);
            for (Book b : books) {
                result.add(String.format("%s (%s %s) ", b.getTitle(), b.getAuthor().getFirstName(), b.getAuthor().getLastName()));
            }

        }


        return result;

    }

    //Task09
    @Override
    public int getBooksCount(int number) {
        List<Book> books = this.bookRepository.findAll();
        int count = 0;
        for (Book b : books) {
            if (b.getTitle().length() >= number) {
                count++;
            }
        }
        //  List<Book> books = this.bookRepository.findAllByTitleGreaterThan(number);
        return count;
    }

    //Task10
    @Override
    public List<AuthorDTO> getTotalBooksCopies() {

        List<AuthorDTO> result = new ArrayList<AuthorDTO>();
        List<Author> authors = this.authorRepository.findAll();
        int copies = 0;
        for (Author a : authors) {
            Set<Book> books = a.getBooks();
            for (Book b : books) {
                copies += b.getCopies();
            }
            result.add(new AuthorDTO(a.getFirstName(), a.getLastName(), copies));
            copies = 0;

        }
        result.sort((AuthorDTO a1, AuthorDTO a2) -> a2.getCopiesCount() - a1.getCopiesCount());

        return result;
    }

    //Task11
    @Override
    public BookDTO getBooksByTitle(String text) {
        Book book = this.bookRepository.findBookByTitle(text);

        BookDTO bookDTO = this.modelMapper.map(book, BookDTO.class);
        return bookDTO;
    }

    private Set<Category> getRandomCategories() {
        Set<Category> categories = new LinkedHashSet<>();

        Random random = new Random();
        int length = random.nextInt(5);

        for (int i = 0; i < length; i++) {
            Category category = this.getRandomCategory();

            categories.add(category);
        }

        return categories;
    }

    private Category getRandomCategory() {
        Random random = new Random();

        int randomId = random.nextInt((int) (this.categoryRepository.count() - 1)) + 1;

        return this.categoryRepository.findById(randomId).orElse(null);
    }
}
