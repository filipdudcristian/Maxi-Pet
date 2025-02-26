package com.example.MaxiPet.controller;

import com.example.MaxiPet.dto.ProductDTO;
import com.example.MaxiPet.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@CrossOrigin
@Validated
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService)
    {
        this.productService = productService;
    }

    /**
     *
     * @param productDTO the dto of the product to be created
     * @return ResponseEntity containing the created ProductDTO with HTTP status code 201 (Created) if successful.
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO) throws Exception {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     *
     * @param productDTO contains the data required to update the product information
     * @return ResponseEntity containing the updated ProductDTO with HTTP status code 200 (OK) if successful
     *         or HTTP status code 400 (Bad Request) otherwise.
     */
    @PutMapping
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDTO productDTO) throws Exception {
        ProductDTO updatedProduct = productService.updateProduct(productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }

    /**
     *
     * @param productId contains the id of the product to be retrieved
     * @return ResponseEntity containing the ProductDTO with HTTP status code 200 (OK) if the product is found
     *         or HTTP status code 400 (Bad Request) otherwise.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Integer productId) throws Exception {
        ProductDTO productDTO = productService.getProduct(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }

    /**
     *
     * @return
     */
    @GetMapping(value = "/json")
    public ResponseEntity<?> getAllProduct() {
        List<ProductDTO> productDTO = productService.getAllProducts();
        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }

    /**
     *
     * @return
     */
    @GetMapping("/products")
    public ModelAndView adminPageProducts()
    {
        ModelAndView modelAndView = new ModelAndView("/AdminPage/products");
        List<ProductDTO> products = productService.getAllProducts();
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    /**
     *
     * @param productId
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/delete/{id}")
    public ModelAndView deleteProductPage(@PathVariable("id") Integer productId) throws Exception {
        productService.deleteProduct(productId);
        return new ModelAndView("redirect:/product/products");
    }

    /**
     *
     * @param productDTO
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/update")
    public ModelAndView updateProductPage(@Valid @ModelAttribute(name = "product") ProductDTO productDTO) throws Exception {
        ProductDTO product = productService.updateProduct(productDTO);
        ModelAndView modelAndView = new  ModelAndView("redirect:/product/products");
        return modelAndView;
    }

    /**
     *
     * @param productDTO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/create")
    public ModelAndView createUserPage(@Valid @ModelAttribute("product") ProductDTO productDTO) throws Exception {
        productService.createProduct(productDTO);
        return new  ModelAndView("redirect:/product/products");
    }

    /**
     *
     * @param productId contains the id of the product to be deleted
     * @return ResponseEntity with HTTP status code 200 (OK) if the product is successfully deleted
     *         or HTTP status code 400 (Bad Request) otherwise.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Integer productId) throws Exception {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/products/client")
    public ModelAndView clientPageProducts(HttpSession session)
    {
        ModelAndView modelAndView = new ModelAndView("/ClientPage/shop_page");
        session.setAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        List<ProductDTO> products = productService.getAllProducts();
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    /**
     * Retrieves products by category.
     * @param category The category to filter products
     * @return ResponseEntity containing a list of ProductDTOs if successful, or HTTP status NOT_FOUND if no products found
     */
    @GetMapping("/byCategory")
    public ModelAndView getProductsByCategory(@RequestParam("category") String category,@RequestParam("sort") String sort) {
        ModelAndView mav=new ModelAndView("ClientPage/shop_page");
        try {
            List<ProductDTO> productsDTO = productService.getAllProductsByCategory(category,sort);
            mav.addObject("products",productsDTO);
            return mav;
        } catch (Exception e) {
            mav.addObject("error","Nu exista niciun produs in aceasta categorie");
            return mav;
        }
    }

    @GetMapping("/search")
    public ModelAndView searchProducts(@RequestParam("query") String query) {
        ModelAndView mav = new ModelAndView("ClientPage/shop_page");
        try {
            List<ProductDTO> productsDTO = productService.searchProducts(query);
            mav.addObject("products", productsDTO);
            return mav;
        } catch (Exception e) {
            mav.addObject("error", "No products found matching the search query.");
            return mav;
        }
    }
}
