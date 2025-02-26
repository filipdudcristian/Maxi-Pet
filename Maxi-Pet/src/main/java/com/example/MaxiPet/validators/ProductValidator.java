package com.example.MaxiPet.validators;

import com.example.MaxiPet.constants.ProductConstants;
import com.example.MaxiPet.dto.ProductDTO;
import com.example.MaxiPet.entity.Product;
import com.example.MaxiPet.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class ProductValidator {

    private final ProductRepository productRepository;

    public void validateProductId(Integer productId) throws Exception {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            log.warn(ProductConstants.PRODUCT_NOT_FOUND, productId);
            throw new Exception(ProductConstants.EXCEPTION_PRODUCT_NOT_FOUND);
        }
    }

    public void validateProductDTOForUpdate(ProductDTO productDTO) throws Exception {
        if (productDTO == null) {
            log.warn(ProductConstants.DTO_EMPTY);
            throw new Exception(ProductConstants.EXCEPTION_DTO_NULL);
        }

        validateProductId(productDTO.getId());
        validateProductFields(productDTO);
    }

    public void validateProductFields(ProductDTO productDTO) throws Exception {
        if (productDTO.getName() == null || productDTO.getName().isEmpty()) {
            log.warn(ProductConstants.NAME_EMPTY);
            throw new Exception(ProductConstants.EXCEPTION_NAME_EMPTY);
        }
        if (productDTO.getBasePrice() == null || productDTO.getBasePrice() < 0) {
            log.warn(ProductConstants.BASE_PRICE_INVALID);
            throw new Exception(ProductConstants.EXCEPTION_BASE_PRICE_INVALID);
        }
        if (productDTO.getDiscountPercent() == null || productDTO.getDiscountPercent() < 0 || productDTO.getDiscountPercent() > 100) {
            log.warn(ProductConstants.DISCOUNT_PERCENT_INVALID);
            throw new Exception(ProductConstants.EXCEPTION_DISCOUNT_PERCENT_INVALID);
        }
        if (productDTO.getStock() == null || productDTO.getStock() < 0) {
            log.warn(ProductConstants.STOCK_INVALID);
            throw new Exception(ProductConstants.EXCEPTION_STOCK_INVALID);
        }
        if (productDTO.getCategory() == null || productDTO.getCategory().isEmpty()) {
            log.warn(ProductConstants.CATEGORY_EMPTY);
            throw new Exception(ProductConstants.EXCEPTION_CATEGORY_EMPTY);
        }
    }
}
