package com.example.MaxiPet.validators;

import com.example.MaxiPet.constants.ShoppingCartProductConstants;
import com.example.MaxiPet.dto.ShoppingCartProductDTO;
import com.example.MaxiPet.entity.ShoppingCartProduct;
import com.example.MaxiPet.repository.ShoppingCartProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class ShoppingCartProductValidator {

    private final ShoppingCartProductRepository shoppingCartProductRepository;

    public void validateShoppingCartProductId(Integer shoppingCartProductId) throws Exception {
        Optional<ShoppingCartProduct> shoppingCartProduct = shoppingCartProductRepository.findById(shoppingCartProductId);
        if (shoppingCartProduct.isEmpty()) {
            log.warn(ShoppingCartProductConstants.PRODUCT_NOT_FOUND, shoppingCartProductId);
            throw new Exception(ShoppingCartProductConstants.EXCEPTION_PRODUCT_NOT_FOUND);
        }
    }

    public void validateShoppingCartProductDTOForUpdate(ShoppingCartProductDTO shoppingCartProductDTO) throws Exception {
        if (shoppingCartProductDTO == null) {
            log.warn(ShoppingCartProductConstants.DTO_EMPTY);
            throw new Exception(ShoppingCartProductConstants.EXCEPTION_DTO_NULL);
        }

        validateShoppingCartProductId(shoppingCartProductDTO.getId());
        validateShoppingCartProductFields(shoppingCartProductDTO);
    }

    public void validateShoppingCartProductFields(ShoppingCartProductDTO shoppingCartProductDTO) throws Exception {
        if (shoppingCartProductDTO.getQuantity() == null || shoppingCartProductDTO.getQuantity() < 0) {
            log.warn(ShoppingCartProductConstants.QUANTITY_INVALID);
            throw new Exception(ShoppingCartProductConstants.EXCEPTION_QUANTITY_INVALID);
        }
    }
}
