package cardealer.service;

import cardealer.domain.dtos.CustomerSeedDto;
import cardealer.domain.dtos.OrderCustomerDto;
import cardealer.domain.entities.Customer;
import cardealer.repository.CustomerRepository;
import cardealer.util.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(ValidatorUtil validatorUtil, ModelMapper modelMapper, CustomerRepository customerRepository) {
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
        this.customerRepository = customerRepository;
    }


    @Override
    public void seedCustomers(CustomerSeedDto[] customerSeedDtos) {
        for (CustomerSeedDto customerSeedDto : customerSeedDtos) {
            if (!this.validatorUtil.isValid(customerSeedDto)) {
                this.validatorUtil.violations(customerSeedDto).forEach(violation -> {
                    System.out.println(violation.getMessage());
                });

                continue;
            }

            Customer entity = this.modelMapper.map(customerSeedDto, Customer.class);
            entity.setBirthDate(LocalDateTime.parse(customerSeedDto.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            System.out.println(entity.getBirthDate());
            this.customerRepository.saveAndFlush(entity);

        }
    }

    @Override
    public List<OrderCustomerDto> getOrderCustomers() {
        List<OrderCustomerDto> result = new ArrayList<>();
        List<Customer> entity = this.customerRepository.findAll();
        for (Customer c : entity) {
            OrderCustomerDto orderCustomerDto = this.modelMapper.map(c, OrderCustomerDto.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formatDateTime = c.getBirthDate().format(formatter);
            orderCustomerDto.setBirthDate(formatDateTime);

            result.add(orderCustomerDto);
        }
        result.sort((OrderCustomerDto o1, OrderCustomerDto o2) -> o1.getBirthDate().compareTo(o2.getBirthDate()));
        return result;
    }
}
