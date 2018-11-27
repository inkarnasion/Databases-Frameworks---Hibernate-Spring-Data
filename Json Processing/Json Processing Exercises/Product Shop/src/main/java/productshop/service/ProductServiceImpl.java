package productshop.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import productshop.domain.dtos.*;
import productshop.domain.entities.Category;
import productshop.domain.entities.Product;
import productshop.domain.entities.User;
import productshop.repository.CategoryRepository;
import productshop.repository.ProductRepository;
import productshop.repository.UserRepository;
import productshop.util.Tools;
import productshop.util.ValidatorUtil;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository, ValidatorUtil validatorUtil, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedProducts(ProductSeedDto[] productSeedDtos) {
        for (ProductSeedDto productSeedDto : productSeedDtos) {
            if (!this.validatorUtil.isValid(productSeedDto)) {
                this.validatorUtil.violations(productSeedDto).forEach(violation -> {
                    System.out.println(violation.getMessage());
                });

                continue;
            }

            Product entity = this.modelMapper.map(productSeedDto, Product.class);
            entity.setSeller(this.getRandomUser());

            Random random = new Random();

            if (random.nextInt() % 13 != 0) {
                entity.setBuyer(this.getRandomUser());
            }

            entity.setCategories(this.getRandomCategories());

            this.productRepository.saveAndFlush(entity);
        }
    }

    @Override
    public List<ProductInRangeDto> productsInRange(BigDecimal more, BigDecimal less) {
        List<Product> productEntities = this.productRepository.findAllByPriceBetweenAndBuyerOrderByPrice(more, less, null);

        List<ProductInRangeDto> productInRangeDtos = new ArrayList<>();
        for (Product productEntity : productEntities) {
            ProductInRangeDto productInRangeDto = this.modelMapper.map(productEntity, ProductInRangeDto.class);
            productInRangeDto.setSeller(String.format("%s %s", productEntity.getSeller().getFirstName(), productEntity.getSeller().getLastName()));

            productInRangeDtos.add(productInRangeDto);
        }

        return productInRangeDtos;
    }

    @Override
    public List<UserSoldProductsDto> successfullySoldProducts() {
        List<UserSoldProductsDto> userSoldedProducts = new ArrayList<>();
        List<SoldProductsDto> soldedProducts;

        List<Product> productEntities = this.productRepository.findAllWithBuyerIsNotNullAndSellerIsNotNull();

        for (Product productEntity : productEntities) {

            SoldProductsDto soldProduct = this.modelMapper.map(productEntity, SoldProductsDto.class);
            soldProduct.setBuyerFirstName(productEntity.getBuyer().getFirstName());
            soldProduct.setBuyerLastName(productEntity.getBuyer().getLastName());

            UserSoldProductsDto seller = new UserSoldProductsDto();
            seller.setId(productEntity.getSeller().getId());

            UserSoldProductsDto sellerFound = Tools.getElementFromArray(seller, userSoldedProducts);

            if (sellerFound != null) {
                sellerFound.getSoldProducts().add(soldProduct);
            } else {
                seller.setFirstName(productEntity.getSeller().getFirstName());
                seller.setLastName(productEntity.getSeller().getLastName());
                seller.getSoldProducts().add(soldProduct);
                userSoldedProducts.add(seller);
            }

        }
        userSoldedProducts.sort(Comparator.comparing(UserSoldProductsDto::getLastName));
        return userSoldedProducts;
    }

    @Override
    public List<UserAgeSoldProductsDto> userSoldProducts() {
        List<UserAgeSoldProductsDto> userSoldProducts = new ArrayList<>();
        //1.Get product info from DB
        List<Product> entityProducts = this.productRepository.findAllWithBuyerIsNotNullAndSellerIsNotNull();

        for (Product entityProduct : entityProducts) {
            ProductDto productDto = this.modelMapper.map(entityProduct, ProductDto.class);
            productDto.setName(entityProduct.getName());
            productDto.setPrice(entityProduct.getPrice());
            //2.Get seller info
            UserAgeSoldProductsDto seller = new UserAgeSoldProductsDto();
            seller.setId(entityProduct.getSeller().getId());
            UserAgeSoldProductsDto sellerFound = Tools.getElementFromArray(seller, userSoldProducts);
            //3. Check is seller exist
            if (sellerFound != null) {
                //4.1. Get current sold products list
                ProductsDto currentsSoldProduct = sellerFound.getSoldProducts();
                currentsSoldProduct.addProduct(productDto);
            } else {
                seller.setFirstName(entityProduct.getSeller().getFirstName());
                seller.setLastName(entityProduct.getSeller().getLastName());
                seller.setAge(entityProduct.getSeller().getAge());
                //4.1. Set new sold products list
                ProductsDto soldProduct = new ProductsDto();
                soldProduct.addProduct(productDto);

                seller.setSoldProducts(soldProduct);
                userSoldProducts.add(seller);
            }
        }
        userSoldProducts.sort((UserAgeSoldProductsDto u1, UserAgeSoldProductsDto u2) -> u2.getSoldProducts().getProductsCount() - u1.getSoldProducts().getProductsCount());
        return userSoldProducts;

    }

    private User getRandomUser() {
        Random random = new Random();

        return this.userRepository.getOne(random.nextInt((int) this.userRepository.count() - 1) + 1);
    }

    private List<Category> getRandomCategories() {
        Random random = new Random();

        List<Category> categories = new ArrayList<>();

        int categoriesCount = random.nextInt((int) this.categoryRepository.count() - 1) + 1;

        for (int i = 0; i < categoriesCount; i++) {
            categories.add(this.categoryRepository.getOne(random.nextInt((int) this.categoryRepository.count() - 1) + 1));
        }

        return categories;
    }


}
