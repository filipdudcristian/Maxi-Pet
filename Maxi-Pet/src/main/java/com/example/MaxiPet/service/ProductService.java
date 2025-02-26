package com.example.MaxiPet.service;

import com.example.MaxiPet.dto.Builders.ProductBuilder;
import com.example.MaxiPet.dto.ProductDTO;
import com.example.MaxiPet.entity.Product;
import com.example.MaxiPet.repository.ProductRepository;
import com.example.MaxiPet.validators.ProductValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductValidator productValidator)
    {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    /**
     *
     * @param productDTO The ProductDTO containing information about the product to be created.
     * @return A ProductDTO representing the newly created product.
     */
    public ProductDTO createProduct(ProductDTO productDTO) throws Exception {
        productValidator.validateProductFields(productDTO);
        Product product = productRepository.save(ProductBuilder.toEntity(productDTO));
        return ProductBuilder.toProductDTO(product);
    }

    /**
     *
     * @param productDTO contains the product of the user to be updated
     * @return the productDTO of the updated product entity
     * @throws Exception if the product is not found
     */
    public ProductDTO updateProduct(ProductDTO productDTO) throws Exception {
        productValidator.validateProductDTOForUpdate(productDTO);

        Optional<Product> optionalProduct = productRepository.findById(productDTO.getId());

        Product product = optionalProduct.get();

        product.setName(productDTO.getName());
        product.setBasePrice(productDTO.getBasePrice());
        product.setDiscountPercent(productDTO.getDiscountPercent());
        product.setCategory(productDTO.getCategory());
        product.setStock(productDTO.getStock());
        product.setImage(productDTO.getImage());
        Product updatedProduct = productRepository.save(product);

        return ProductBuilder.toProductDTO(updatedProduct);
    }

    /**
     *
     * @param productId  contains the id of the product to be retrieved
     * @return the productDTO of the retrieved product entity
     * @throws Exception if the product is not found
     */
    public ProductDTO getProduct(Integer productId) throws Exception {
        productValidator.validateProductId(productId);
        Optional<Product> product = productRepository.findById(productId);

        return ProductBuilder.toProductDTO(product.get());
    }

    /**
     *
     * @return
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .map(ProductBuilder::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param productId the id of the product to be deleted
     * @throws Exception if the product is not found
     */
    public void deleteProduct(Integer productId) throws Exception {
        productValidator.validateProductId(productId);
        Optional<Product> product = productRepository.findById(productId);
        productRepository.deleteById(productId);
    }

    /**
     * Retrieves all products belonging to a specific category.
     * @param category The category of products to retrieve.
     * @return A list of {@link ProductDTO} objects representing the products.
     * @throws Exception If no products are found for the given category.
     */
    public List<ProductDTO> getAllProductsByCategory(String category,String sort) throws Exception {
        List<Product> productList;
        if(category.equals("All"))
            productList = (List<Product>) productRepository.findAll();
        else
            productList = productRepository.findByCategory(category);

        if(productList.isEmpty()) {
            log.error("No products found for category {}", category);

            throw new Exception("No products found for category: " + category);
        }
        // Sort the products based on the sort parameter
        if ("ASC".equalsIgnoreCase(sort)) {
            productList.sort(Comparator.comparing(Product::getDiscountedPrice));
        } else if ("DESC".equalsIgnoreCase(sort)) {
            productList.sort(Comparator.comparing(Product::getDiscountedPrice).reversed());
        }
        return productList.stream()
                .map(ProductBuilder::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     * Searches for products based on the provided query string.
     * @param query The search query string
     * @return List of ProductDTOs matching the search query.
     * @throws Exception If an error occurs during the search operation.
     */
    public List<ProductDTO> searchProducts(String query) throws Exception {

        List<Product> productList;

        if(query.isEmpty())
            productList = (List<Product>) productRepository.findAll();
        else {
            query = query.toLowerCase();
            query = query.substring(0, 1).toUpperCase() + query.substring(1);
            productList = productRepository.findProductByNameContaining(query);
            if (productList.isEmpty()) {
                throw new Exception("No products found matching the search query.");
            }
        }
        return productList.stream()
                .map(ProductBuilder::toProductDTO)
                .collect(Collectors.toList());
    }
}
