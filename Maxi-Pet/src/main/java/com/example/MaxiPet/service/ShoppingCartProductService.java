package com.example.MaxiPet.service;

import com.example.MaxiPet.dto.Builders.ShoppingCartProductBuilder;
import com.example.MaxiPet.dto.ShoppingCartProductDTO;
import com.example.MaxiPet.entity.Product;
import com.example.MaxiPet.entity.ShoppingCart;
import com.example.MaxiPet.entity.ShoppingCartProduct;
import com.example.MaxiPet.entity.User;
import com.example.MaxiPet.repository.ProductRepository;
import com.example.MaxiPet.repository.ShoppingCartProductRepository;
import com.example.MaxiPet.repository.ShoppingCartRepository;
import com.example.MaxiPet.repository.UserRepository;
import com.example.MaxiPet.validators.ShoppingCartProductValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ShoppingCartProductService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final UserRepository userRepository;
    private final ShoppingCartProductValidator shoppingCartProductValidator;


    @Autowired
    public ShoppingCartProductService(ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository, ShoppingCartProductRepository shoppingCartProductRepository, UserRepository userRepository, ShoppingCartProductValidator shoppingCartProductValidator)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.shoppingCartProductRepository = shoppingCartProductRepository;
        this.userRepository = userRepository;
        this.shoppingCartProductValidator = shoppingCartProductValidator;
    }
    /**
     *
     * @param shoppingCartProductDTO the dto that contains the id of the user, the id of the product and the quantity
     * @return the dto of the product from the shopping cart
     * @throws Exception if the user or the product id are not found
     */
    public ShoppingCartProductDTO addProductToShoppingCartOfUser(ShoppingCartProductDTO shoppingCartProductDTO) throws Exception
    {
        shoppingCartProductValidator.validateShoppingCartProductFields(shoppingCartProductDTO);
        Integer shoppingCartId = shoppingCartProductDTO.getShoppingCartId();
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if(optionalShoppingCart.isEmpty()) {
            log.warn("The shopping cart with id {} was not found in the DB!", shoppingCartProductDTO.getShoppingCartId());
            throw new Exception("Shopping cart not found");
        }

        Optional<Product> optionalProduct = productRepository.findById(shoppingCartProductDTO.getProductId());
        if(optionalProduct.isEmpty()) {
            log.warn("The product with id {} was not found in the DB!", shoppingCartProductDTO.getProductId());
            throw new Exception("Product not found");
        }

        Product product = optionalProduct.get();
        if(product.getStock() < shoppingCartProductDTO.getQuantity()){
            log.warn("The product {} with {} items in stock doesn't have the {} items requested!", product.getName(), product.getStock(), shoppingCartProductDTO.getQuantity());
            throw new Exception("The product " + product.getName() + " with " + product.getStock() + " items in stock doesn't have the " + shoppingCartProductDTO.getQuantity() + " items requested!");

        }

        ShoppingCartProduct newShoppingCartProduct = ShoppingCartProduct.builder()
                .shoppingCart(optionalShoppingCart.get())
                .product(optionalProduct.get())
                .quantity(shoppingCartProductDTO.getQuantity())
                .build();

        ShoppingCartProduct shoppingCartProduct = shoppingCartProductRepository.save(newShoppingCartProduct);

        ShoppingCart updatedShoppingCart = shoppingCartRepository.findById(shoppingCartProductDTO.getShoppingCartId()).get();
        updatedShoppingCart.setTotalPrice(updatedShoppingCart.getTotalPrice() + product.getDiscountedPrice() * shoppingCartProduct.getQuantity());
        shoppingCartRepository.save(updatedShoppingCart);

        return ShoppingCartProductBuilder.toShoppingCartProductDTO(shoppingCartProduct);
    }

    /**
     *
     * @param userId
     * @param productId
     * @param quantity
     * @return
     * @throws Exception
     */
    public ShoppingCartProductDTO addProductToShoppingCartOfUserFrontEnd(Integer userId, Integer productId, Integer quantity) throws Exception
    {
        shoppingCartProductValidator.validateShoppingCartProductFields(ShoppingCartProductDTO.builder().quantity(quantity).build());
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            log.warn("The user with id {} was not found in the DB!", userId);
            throw new Exception("User cart not found");
        }

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty()) {
            log.warn("The product with id {} was not found in the DB!", productId);
            throw new Exception("Product not found");
        }

        Product product = optionalProduct.get();
        if(product.getStock() < quantity){
            log.warn("The product {} with {} items in stock doesn't have the {} items requested!", product.getName(), product.getStock(), quantity);
            throw new Exception("The product " + product.getName() + " with " + product.getStock() + " items in stock doesn't have the " + quantity + " items requested!");
        }

        ShoppingCartProduct newShoppingCartProduct = ShoppingCartProduct.builder()
                .shoppingCart(optionalUser.get().getShoppingCart())
                .product(optionalProduct.get())
                .quantity(quantity)
                .build();

        ShoppingCartProduct shoppingCartProduct = shoppingCartProductRepository.save(newShoppingCartProduct);

        ShoppingCart updatedShoppingCart = shoppingCartRepository.findById(optionalUser.get().getShoppingCart().getId()).get();
        updatedShoppingCart.setTotalPrice(updatedShoppingCart.getTotalPrice() + product.getDiscountedPrice() * shoppingCartProduct.getQuantity());
        shoppingCartRepository.save(updatedShoppingCart);

        return ShoppingCartProductBuilder.toShoppingCartProductDTO(shoppingCartProduct);
    }

    /**
     *
     * @param shoppingCartProductDTO
     * @return
     * @throws Exception
     */
    public ShoppingCartProductDTO updateShoppingCartProduct(ShoppingCartProductDTO shoppingCartProductDTO) throws Exception {
        shoppingCartProductValidator.validateShoppingCartProductDTOForUpdate(shoppingCartProductDTO);
        Optional<ShoppingCartProduct> optionalShoppingCartProduct = shoppingCartProductRepository.findById(shoppingCartProductDTO.getId());

        ShoppingCartProduct shoppingCartProduct = optionalShoppingCartProduct.get();


        if (shoppingCartProduct.getQuantity() > shoppingCartProduct.getProduct().getStock())
        {
            log.warn("The product {} with {} items in stock doesn't have the {} items requested!", shoppingCartProduct.getProduct().getName(), shoppingCartProduct.getProduct().getStock(), shoppingCartProduct.getQuantity());
            throw new Exception("The product " + shoppingCartProduct.getProduct().getName() + " with " + shoppingCartProduct.getProduct().getStock() + " items in stock doesn't have the " + shoppingCartProduct.getQuantity() + " items requested!");
        }

        Integer quantityDiff = shoppingCartProductDTO.getQuantity() - shoppingCartProduct.getQuantity();

        shoppingCartProduct.setQuantity(shoppingCartProductDTO.getQuantity());
        shoppingCartProduct.getShoppingCart().setTotalPrice(shoppingCartProduct.getShoppingCart().getTotalPrice() + (quantityDiff * shoppingCartProduct.getProduct().getDiscountedPrice()) );

        ShoppingCartProduct updatedShoppingCartProduct = shoppingCartProductRepository.save(shoppingCartProduct);

        return ShoppingCartProductBuilder.toShoppingCartProductDTO(updatedShoppingCartProduct);
    }

    /**
     *
     * @param shoppingCartProductId
     * @throws Exception
     */
    public void deleteShoppingCartProduct(Integer shoppingCartProductId) throws Exception {
        shoppingCartProductValidator.validateShoppingCartProductId(shoppingCartProductId);

        Optional<ShoppingCartProduct> optionalShoppingCartProduct = shoppingCartProductRepository.findById(shoppingCartProductId);

        ShoppingCartProduct shoppingCartProduct = optionalShoppingCartProduct.get();
        Integer shoppingCartId = shoppingCartProduct.getShoppingCart().getId();
        ShoppingCart updatedShoppingCart = shoppingCartRepository.findById(shoppingCartId).get();
        updatedShoppingCart.setTotalPrice(updatedShoppingCart.getTotalPrice() - shoppingCartProduct.getProduct().getDiscountedPrice() * shoppingCartProduct.getQuantity());
        shoppingCartRepository.save(updatedShoppingCart);

        shoppingCartProductRepository.deleteById(shoppingCartProductId);
    }

    /**
     *
     * @param shoppingCartId
     * @return
     * @throws Exception
     */
    public List<ShoppingCartProductDTO> getAllShoppingCartProductsForShoppingCartId(Integer shoppingCartId) throws Exception{
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if(optionalShoppingCart.isEmpty()) {
            log.warn("The shopping cart with id {} was not found in the DB!", shoppingCartId);
            throw new Exception("Shopping cart not found");
        }

        List<ShoppingCartProduct> shoppingCartProductList = shoppingCartProductRepository.getAllByShoppingCart(optionalShoppingCart.get());
        return ShoppingCartProductBuilder.toShoppingCartProductDTOList(shoppingCartProductList);
    }


}
