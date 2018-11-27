package cardealer.service;



import cardealer.domain.dtos.CustomerSeedDto;
import cardealer.domain.dtos.OrderCustomerDto;

import java.util.List;

public interface CustomerService {

    void seedCustomers(CustomerSeedDto[] customerSeedDtos);

    List<OrderCustomerDto> getOrderCustomers();
}
