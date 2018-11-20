package org.softuni.bookshopsystemapp.controller;


import org.softuni.bookshopsystemapp.domain.dtos.BookDTO;
import org.softuni.bookshopsystemapp.service.AuthorService;
import org.softuni.bookshopsystemapp.service.BookService;
import org.softuni.bookshopsystemapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class BookshopController implements CommandLineRunner {

    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookService bookService;

    @Autowired
    public BookshopController(AuthorService authorService, CategoryService categoryService, BookService bookService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.bookService = bookService;
    }

    /* RUN APP!
        1.In application.property file enter or delete password for database.
        2.Change in BookServiceImpl,AuthorServiceImpl, CategoriesServiceImpl classes files paths!
        3.For test task enter input number between 1 to 14
        4.For some tasks have to enter additional input!
    *
    * */
    @Override
    public void run(String... strings) throws Exception {
        this.authorService.seedAuthors();
        this.categoryService.seedCategories();
        this.bookService.seedBooks();

        this.callTask(getScanner());


    }

    private void callTask(Scanner sc) {
        switch (sc.nextInt()) {
            case 1:
                this.bookService.getBooksByAgeRestiction(getScanner().nextLine()).stream().forEach(b -> System.out.println(b));
                break;
            case 2:
                this.bookService.getGoldenBooks().stream().forEach(b -> System.out.println(b));
                break;
            case 3:
                this.bookService.getBooksInPriceRange().stream().forEach(b -> System.out.println(b));
                break;
            case 4:
                String yearAsString = getScanner().nextLine();

                String result = this.bookService.notReleasedBooks(yearAsString);

                System.out.println(result);

                break;
            case 5:
                this.bookService.getBooksWithReleasedDateBefore(getScanner().nextLine()).stream().forEach(b -> System.out.println(b));
                break;
            case 6:
                this.authorService.getAuthorsByFirstNamePatter(getScanner().nextLine()).stream().forEach(a -> System.out.println(a));
                break;
            case 7:
                this.bookService.getBooksByTitlePattern(getScanner().nextLine()).stream().forEach(b -> System.out.println(b));
                break;
            case 8:
                this.bookService.getBooksByAuthorWithLastNameStartWith(getScanner().nextLine()).stream().forEach(e -> System.out.println(e));
                break;
            case 9:
                int input = Integer.parseInt(getScanner().nextLine());
                int count = this.bookService.getBooksCount(input);
                System.out.println(count);
                System.out.printf("There are %d books with longer title than %d symbols\n", count, input);
                break;
            case 10:
                this.bookService.getTotalBooksCopies().stream().forEach(e -> System.out.println(e));
                break;
            case 11:
                BookDTO bookDto = this.bookService.getBooksByTitle(getScanner().nextLine());
                System.out.println(bookDto.toString());

            default:
                System.out.println("Invalid Input!!!");
                break;


        }
    }

    private static Scanner getScanner() {
        return new Scanner(System.in);
    }
}
