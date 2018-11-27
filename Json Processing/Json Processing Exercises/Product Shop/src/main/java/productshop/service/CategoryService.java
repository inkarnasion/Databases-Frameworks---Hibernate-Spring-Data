package productshop.service;



import productshop.domain.dtos.CategoriesByProductsDto;
import productshop.domain.dtos.CategorySeedDto;

import java.util.List;


public interface CategoryService {

    void seedCategories(CategorySeedDto[] categorySeedDtos);

    List<CategoriesByProductsDto> getCategoriesByProductsCount();
}
