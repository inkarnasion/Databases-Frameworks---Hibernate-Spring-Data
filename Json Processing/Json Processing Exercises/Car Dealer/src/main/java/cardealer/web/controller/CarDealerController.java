package cardealer.web.controller;

import com.google.gson.Gson;
import cardealer.domain.dtos.*;
import cardealer.domain.entities.Supplier;
import cardealer.service.*;
import cardealer.util.FileIOUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Controller
public class CarDealerController implements CommandLineRunner {
    private final static String SUPPLER_FILE_PATH = "D:\\Java\\CarDealerJSON\\src\\main\\resources\\files\\suppliers.json";
    private final static String PART_FILE_PATH = "D:\\Java\\CarDealerJSON\\src\\main\\resources\\files\\parts.json";
    private final static String CARS_FILE_PATH = "D:\\Java\\CarDealerJSON\\src\\main\\resources\\files\\cars.json";
    private final static String CUSTOMER_PATH = "D:\\Java\\CarDealerJSON\\src\\main\\resources\\files\\customers.json";
    private final static String ORDER_CUSTOMER_PATH = "D:\\Java\\CarDealerJSON\\src\\main\\resources\\files\\output\\order-customers.json";
    private final static String TOYOTA_CARS_PATH = "D:\\Java\\CarDealerJSON\\src\\main\\resources\\files\\output\\toyota-cars.json";
    private final static String LOCAL_SUPPLIERS_PATH = "D:\\Java\\CarDealerJSON\\src\\main\\resources\\files\\output\\local-suppliers.json";
    private final FileIOUtil fileIOUtil;
    private final Gson gson;
    private final Scanner scanner;
    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final SaleService saleService;

    @Autowired

    public CarDealerController(FileIOUtil fileIOUtil, Gson gson, Scanner scanner, SupplierService supplierService, PartService partService, CarService carService, CustomerService customerService, SaleService saleService) {
        this.fileIOUtil = fileIOUtil;
        this.gson = gson;
        this.scanner = scanner;
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
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
                this.seedSuppliers();
                break;
            case 2:
                this.seedParts();
                break;
            case 3:
                this.seedCars();
                break;
            case 4:
                this.seedCustomers();
                break;
            case 5:
                this.seedSales();
                break;
            case 6:
                this.getOrderedCustomers();
                break;
            case 7:
                this.getCarsByMake();
                break;
            case 8:
                this.getLocalSupplier();
                break;
            case 0:
                System.out.println("Product shop App was terminated!");
                break;
        }
    }

    private void getLocalSupplier() throws IOException{
        List<SupplierDto> supplierDtos = this.supplierService.getLocalSuppliers();

        String localSuppliersJson = this.gson.toJson(supplierDtos);
        this.fileIOUtil.writeFile(LOCAL_SUPPLIERS_PATH, localSuppliersJson);
    }

    private void getCarsByMake() throws IOException {
        List<CarDto> cars = this.carService.getCarsByMake();

        String toyotaCarsJson = this.gson.toJson(cars);
        this.fileIOUtil.writeFile(TOYOTA_CARS_PATH, toyotaCarsJson);
    }

    private void getOrderedCustomers() throws IOException {
        List<OrderCustomerDto> customers = this.customerService.getOrderCustomers();

        String orderCustomersJson = this.gson.toJson(customers);
        this.fileIOUtil.writeFile(ORDER_CUSTOMER_PATH, orderCustomersJson);
    }

    private void seedSales() {
        this.saleService.seedSales();
    }

    private void seedCustomers() throws IOException {
        String customerFileContent = this.fileIOUtil.readFile(CUSTOMER_PATH);
        CustomerSeedDto[] customerSeedDtos = this.gson.fromJson(customerFileContent, CustomerSeedDto[].class);
        this.customerService.seedCustomers(customerSeedDtos);
    }

    private void seedCars() throws IOException {
        String carFileContent = this.fileIOUtil.readFile(CARS_FILE_PATH);
        CarSeedDto[] carSeedDtos = this.gson.fromJson(carFileContent, CarSeedDto[].class);
        this.carService.seedCars(carSeedDtos);

    }

    private void seedParts() throws IOException {
        String partFileContent = this.fileIOUtil.readFile(PART_FILE_PATH);
        PartSeedDto[] partSeedDtos = this.gson.fromJson(partFileContent, PartSeedDto[].class);
        this.partService.seedParts(partSeedDtos);

    }

    private void seedSuppliers() throws IOException {
        String suppliersFileContent = this.fileIOUtil.readFile(SUPPLER_FILE_PATH);
        SupplierSeedDto[] supplierSeedDtos = this.gson.fromJson(suppliersFileContent, SupplierSeedDto[].class);
        this.supplierService.seedSuppliers(supplierSeedDtos);
    }

    private String startAppInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Enter number:\n")
                .append("It is mandatory numbers 1,2,3,4 and 5 to be executed at least once!\n")
                .append("1.Insert suppliers info.\n")
                .append("2.Insert parts info\n")
                .append("3.Insert cars info\n")
                .append("4.Insert customers info.\n")
                .append("5.Insert sales info\n")
                .append("6 Get ordered customers from DB.\n")
                .append("7.Get cars make Toyota from DB.\n")
                .append("8.Get local supplier from DB.\n")
                .append("0. Terminate App!\n").append("Check query results into resources/files/output... Path.If exist that path... ");

        return sb.toString();
    }
}
