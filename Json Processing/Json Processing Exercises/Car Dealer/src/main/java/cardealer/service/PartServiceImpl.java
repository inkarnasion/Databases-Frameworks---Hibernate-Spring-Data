package cardealer.service;

import cardealer.domain.dtos.PartSeedDto;
import cardealer.domain.entities.Part;
import cardealer.domain.entities.Supplier;
import cardealer.repository.PartRepository;
import cardealer.repository.SupplierRepository;
import cardealer.util.Tools;
import cardealer.util.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PartServiceImpl implements PartService {

    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;
    private final PartRepository partRepository;
    private final SupplierRepository supplierRepository;

    public PartServiceImpl(ValidatorUtil validatorUtil, ModelMapper modelMapper, PartRepository partRepository, SupplierRepository supplierRepository) {
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
        this.partRepository = partRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void seedParts(PartSeedDto[] partSeedDtos) {
        for (PartSeedDto partSeedDto : partSeedDtos) {
            if (!this.validatorUtil.isValid(partSeedDto)) {
                this.validatorUtil.violations(partSeedDto).forEach(violation -> {
                    System.out.println(violation.getMessage());
                });

                continue;
            }

            Part entity = this.modelMapper.map(partSeedDto, Part.class);
            entity.setSupplier(Tools.getRandomIndex(this.supplierRepository));

            this.partRepository.saveAndFlush(entity);
        }

    }



}
