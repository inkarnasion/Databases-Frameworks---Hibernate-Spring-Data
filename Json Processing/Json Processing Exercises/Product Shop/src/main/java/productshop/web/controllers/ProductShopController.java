package productshop.web.controllers;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import productshop.domain.dtos.*;
import productshop.service.CategoryService;
import productshop.service.ProductService;
import productshop.service.UserService;
import productshop.util.FileIOUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;


@Controller
public class ProductShopController implements CommandLineRunner {

    private final static String USER_FILE_PATH = "D:\\Java\\ProductShopJSON\\src\\main\\resources\\files\\users.json";
    private final static String CATEGORIES_FILE_PATH = "D:\\Java\\ProductShopJSON\\src\\main\\resources\\files\\categories.json";
    private final static String PRODUCTS_FILE_PATH = "D:\\Java\\ProductShopJSON\\src\\main\\resources\\files\\products.json";
    private final static String PRODUCT_IN_RANGE_PATH = "D:\\Java\\ProductShopJSON\\src\\main\\resources\\files\\output\\products-in-range.json";
    private final static String SUCCESSFULLY_SOLD_PRODUCT_PATH = "C:\\Projects\\softuni\\Databases Frameworks - Hibernate & Spring Data\\09.JSON Processing-Exercises\\ProductsShopApp\\src\\main\\resources\\files\\output\\successfully-sold-products.json";
    private final static String USER_SOLD_PRODUCT_PATH = "D:\\Java\\ProductShopJSON\\src\\main\\resources\\files\\output\\user_sold-products.json";
    private final static String CATEGORIES_BY_PRODUCTS_PATH = "D:\\Java\\ProductShopJSON\\src\\main\\resources\\files\\output\\categories-by-products.json";
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final FileIOUtil fileIOUtil;
    private final Gson gson;
    private final Scanner scanner;

    @Autowired
    public ProductShopController(UserService userService, CategoryService categoryService, ProductService productService, FileIOUtil fileIOUtil, Gson gson, Scanner scanner) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.fileIOUtil = fileIOUtil;
        this.gson = gson;
        this.scanner = scanner;
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println(startAppInfo());
        int input;
        do {
            input = scanner.nextInt();
            callMethods(input);
        } while (input != 0);

    }


    private void callMethods(int number) throws Exception {
        switch (number) {
            case 1:
                this.seedUsers();
                break;
            case 2:
                this.seedCategories();
                break;
            case 3:
                this.seedProducts();
                break;
            case 4:
                this.productsInRange();
                break;
            case 5:
                this.successfullySoldProducts();
                break;
            case 6:
                this.categoryByProductsCount();
                break;
            case 7:
                this.userSoldProducts();
                break;
            case 0:
                System.out.println("Product shop App was terminated!");
                break;
        }
    }

    private void seedUsers() throws IOException {
        String usersFileContent = this.fileIOUtil.readFile(USER_FILE_PATH);

        UserSeedDto[] userSeedDtos = this.gson.fromJson(usersFileContent, UserSeedDto[].class);

        this.userService.seedUsers(userSeedDtos);
    }

    private void seedCategories() throws IOException {
        String categoriesFileContent = this.fileIOUtil.readFile(CATEGORIES_FILE_PATH);

        CategorySeedDto[] categorySeedDtos = this.gson.fromJson(categoriesFileContent, CategorySeedDto[].class);

        this.categoryService.seedCategories(categorySeedDtos);
    }

    private void seedProducts() throws IOException {
        String productsFileContent = this.fileIOUtil.readFile(PRODUCTS_FILE_PATH);

        ProductSeedDto[] productSeedDtos = this.gson.fromJson(productsFileContent, ProductSeedDto[].class);

        this.productService.seedProducts(productSeedDtos);
    }

    private void productsInRange() throws IOException {
        List<ProductInRangeDto> productInRangeDtos = this.productService.productsInRange(BigDecimal.valueOf(500), BigDecimal.valueOf(1000));

        String productsInRangeJson = this.gson.toJson(productInRangeDtos);

        this.fileIOUtil.writeFile(PRODUCT_IN_RANGE_PATH, productsInRangeJson);
    }

    private void successfullySoldProducts() throws Exception {
        List<UserSoldProductsDto> products = this.productService.successfullySoldProducts();

        String successfullySoldProductsJson = this.gson.toJson(products);
        this.fileIOUtil.writeFile(SUCCESSFULLY_SOLD_PRODUCT_PATH, successfullySoldProductsJson);
    }

    private void userSoldProducts() throws Exception {
        List<UserAgeSoldProductsDto> products = this.productService.userSoldProducts();

        String userSoldProductsJson = this.gson.toJson(products);
        this.fileIOUtil.writeFile(USER_SOLD_PRODUCT_PATH, userSoldProductsJson);
    }

    private void categoryByProductsCount() throws Exception {
        List<CategoriesByProductsDto> categories = this.categoryService.getCategoriesByProductsCount();

        String categoriesByProductsCountJson = this.gson.toJson(categories);
        this.fileIOUtil.writeFile(CATEGORIES_BY_PRODUCTS_PATH, categoriesByProductsCountJson);

    }

    private String startAppInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Enter number:\n").append("It is mandatory numbers 1,2 and 3 to be executed at least once!\n").append("1.Insert user info.\n").append("2.Insert category info\n").append("3.Insert product info\n").append("4.Get products in range from DB.\n").append("5.Get successfully sold products from DB.\n").append("6 Get category by products count from DB.\n").append("7.Get user sold products from DB.\n").append("0. Terminate App!\n").append("Check query results into resources/files/output... Path.If exist that path... ");

        return sb.toString();
    }
}
