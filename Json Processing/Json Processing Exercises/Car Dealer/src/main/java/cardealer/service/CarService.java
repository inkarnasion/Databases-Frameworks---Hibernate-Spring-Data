package cardealer.service;



import cardealer.domain.dtos.CarDto;
import cardealer.domain.dtos.CarSeedDto;

import java.util.List;

public interface CarService {
    void seedCars(CarSeedDto[] carSeedDtos);

    List<CarDto> getCarsByMake();
}
