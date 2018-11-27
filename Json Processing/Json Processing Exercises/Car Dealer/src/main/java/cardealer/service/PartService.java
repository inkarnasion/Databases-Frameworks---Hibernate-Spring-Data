package cardealer.service;


import cardealer.domain.dtos.PartSeedDto;

public interface PartService {
    void seedParts(PartSeedDto[] partSeedDtos);
}
