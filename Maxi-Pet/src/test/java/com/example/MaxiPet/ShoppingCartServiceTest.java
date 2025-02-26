package com.example.MaxiPet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.MaxiPet.dto.ShoppingCartProductDTO;
import com.example.MaxiPet.entity.Product;
import com.example.MaxiPet.entity.ShoppingCart;
import com.example.MaxiPet.entity.ShoppingCartProduct;
import com.example.MaxiPet.entity.User;
import com.example.MaxiPet.repository.ProductRepository;
import com.example.MaxiPet.repository.ShoppingCartProductRepository;
import com.example.MaxiPet.repository.ShoppingCartRepository;
import com.example.MaxiPet.repository.UserRepository;
import com.example.MaxiPet.service.ShoppingCartProductService;
import com.example.MaxiPet.validators.ShoppingCartProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ShoppingCartServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartProductValidator shoppingCartProductValidator;

    @InjectMocks
    private ShoppingCartProductService shoppingCartProductService;

    private User user;
    private Product product;
    private ShoppingCart shoppingCart;

    @BeforeEach
    public void setUp() {
        shoppingCart = new ShoppingCart();
        shoppingCart.setId(1);
        shoppingCart.setTotalPrice(0.0F);

        user = new User();
        user.setId(1);
        user.setShoppingCart(shoppingCart);

        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setStock(10);
        product.setDiscountedPrice(10.0F);
    }

    @Test
    public void testAddProductToShoppingCartOfUserFrontEnd_Success() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(shoppingCartProductRepository.save(any(ShoppingCartProduct.class))).thenAnswer(i -> i.getArgument(0));
        when(shoppingCartRepository.findById(1)).thenReturn(Optional.of(shoppingCart));

        ShoppingCartProductDTO result = shoppingCartProductService.addProductToShoppingCartOfUserFrontEnd(1, 1, 5);

        assertNotNull(result);
        assertEquals(1, result.getProductDTO().getId());
        assertEquals(5, result.getQuantity());
        assertEquals(50.0F, shoppingCart.getTotalPrice());

        verify(shoppingCartProductValidator).validateShoppingCartProductFields(any(ShoppingCartProductDTO.class));
        verify(userRepository).findById(1);
        verify(productRepository).findById(1);
        verify(shoppingCartProductRepository).save(any(ShoppingCartProduct.class));
        verify(shoppingCartRepository).findById(1);
        verify(shoppingCartRepository).save(shoppingCart);
    }

}
