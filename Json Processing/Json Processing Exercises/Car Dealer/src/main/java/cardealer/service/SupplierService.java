package cardealer.service;

;

import cardealer.domain.dtos.SupplierDto;
import cardealer.domain.dtos.SupplierSeedDto;

import java.util.List;

public interface SupplierService {
    void seedSuppliers(SupplierSeedDto[] supplierSeedDtos);

    List<SupplierDto> getLocalSuppliers();
}
