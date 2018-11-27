package cardealer.service;

import cardealer.domain.entities.Car;
import cardealer.domain.entities.Customer;
import cardealer.domain.entities.Sale;
import cardealer.repository.CarRepository;
import cardealer.repository.CustomerRepository;
import cardealer.repository.SaleRepository;
import cardealer.util.Tools;
import org.springframework.stereotype.Service;

@Service
public class SaleServiceImpl implements SaleService {
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;

    public SaleServiceImpl(CarRepository carRepository, CustomerRepository customerRepository, SaleRepository saleRepository) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public void seedSales() {
        for (int i = 0; i <= 5; i++) {
            Car randomCar = Tools.getRandomIndex(this.carRepository);
            Customer randomCustomer = Tools.getRandomIndex(this.customerRepository);
            double randomDiscounts = getRandomDiscount(Tools.getRandomNumber(1, 8));
            Sale entity = new Sale();
            entity.setCar(randomCar);
            entity.setCustomer(randomCustomer);
            entity.setDiscount(randomDiscounts);

            this.saleRepository.saveAndFlush(entity);
        }


    }

    private double getRandomDiscount(int count) {
        double result = 0.0;

        switch (count) {
            case 1:
                result = 0.0;
                break;

            case 2:
                result = 0.05;
                break;
            case 3:
                result = 0.10;
                break;
            case 4:
                result = 0.15;
                break;
            case 5:
                result = 0.20;
                break;
            case 6:
                result = 0.30;
                break;
            case 7:
                result = 0.40;
                break;
            case 8:
                result = 0.50;
                break;
            default:
                break;
        }
        return result;
    }
}
