package cardealer.service;


import cardealer.domain.dtos.CarDto;
import cardealer.domain.dtos.CarSeedDto;
import cardealer.domain.entities.Car;
import cardealer.repository.CarRepository;
import cardealer.repository.PartRepository;
import cardealer.util.Tools;
import cardealer.util.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    private static final String MAKE = "Toyota";
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;
    private final CarRepository carRepository;
    private final PartRepository partRepository;

    public CarServiceImpl(ValidatorUtil validatorUtil, ModelMapper modelMapper, CarRepository carRepository, PartRepository partRepository) {
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
        this.carRepository = carRepository;
        this.partRepository = partRepository;
    }


    @Override
    public void seedCars(CarSeedDto[] carSeedDtos) {
        for (CarSeedDto carSeedDto : carSeedDtos) {
            if (!this.validatorUtil.isValid(carSeedDtos)) {
                this.validatorUtil.violations(carSeedDtos).forEach(violation -> {
                    System.out.println(violation.getMessage());
                });

                continue;
            }

            Car entity = this.modelMapper.map(carSeedDto, Car.class);
            int count = Tools.getRandomNumber(10,20);
            entity.setParts(Tools.getRandomList(this.partRepository,count));

            this.carRepository.saveAndFlush(entity);
        }
    }

    @Override
    public List<CarDto> getCarsByMake() {
        List<CarDto> result = new ArrayList<>();
        List<Car> entity = this.carRepository.findAllByMake(MAKE);
        for (Car c : entity) {
            CarDto carDto = this.modelMapper.map(c, CarDto.class);



            result.add(carDto);
        }
       result.sort(Comparator.comparing(CarDto::getModel).thenComparing((CarDto c1,CarDto c2)->(int)(c2.getTravelledDistance()-c1.getTravelledDistance())));

        return result;

    }
}
