package com.example.MaxiPet.controller;

import com.example.MaxiPet.dto.ShoppingCartProductDTO;
import com.example.MaxiPet.dto.UserDTO;
import com.example.MaxiPet.service.ShoppingCartProductService;
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
@RequestMapping(value = "/shoppingcartproduct")
public class ShoppingCartProductController {


    private final ShoppingCartProductService shoppingCartProductService;


    @Autowired
    public ShoppingCartProductController(ShoppingCartProductService shoppingCartProductService) {
        this.shoppingCartProductService = shoppingCartProductService;
    }


    /**
     * @param shoppingCartProductDTO contains the data required to add a product to a users shopping cart
     * @return ResponseEntity containing the added ShoppingCartProductDTO with HTTP status code 201 (Created) if successful,
     * or HTTP status code 400 (Bad Request) otherwise.
     */
    @PostMapping
    public ResponseEntity<?> addShoppingCartProduct(@Valid @RequestBody ShoppingCartProductDTO shoppingCartProductDTO) throws Exception {
        ShoppingCartProductDTO newShoppingCartProductDTO = shoppingCartProductService.addProductToShoppingCartOfUser(shoppingCartProductDTO);
        return new ResponseEntity<>(newShoppingCartProductDTO, HttpStatus.CREATED);

    }

    /**
     *
     * @param shoppingCartId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getShoppingCartProductsForShoppingCartId(@PathVariable("id") Integer shoppingCartId) throws Exception {
        List<ShoppingCartProductDTO> shoppingCartProductDTOList = shoppingCartProductService.getAllShoppingCartProductsForShoppingCartId(shoppingCartId);
        return new ResponseEntity<>(shoppingCartProductDTOList, HttpStatus.OK);

    }


    /**
     *
     * @param shoppingCartProductId
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteShoppingCartProduct(@PathVariable("id") Integer shoppingCartProductId) throws Exception {
        shoppingCartProductService.deleteShoppingCartProduct(shoppingCartProductId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param userId
     * @param productId
     * @param quantity
     * @return
     * @throws Exception
     */
    @PostMapping("/addproduct")
    public ModelAndView addShoppingCartProductClient2(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "productId") Integer productId, @RequestParam(name = "quantity") Integer quantity, HttpSession session) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        session.setAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        try {
            shoppingCartProductService.addProductToShoppingCartOfUserFrontEnd(userId, productId, quantity);
            modelAndView.setViewName("redirect:/product/products/client");
            return modelAndView;
        } catch (Exception e) {
            modelAndView.setViewName("redirect:/product/products/client");
            modelAndView.addObject("error", "Quantity exceeds stock");
            return modelAndView;
        }
    }


    /**
     *
     * @param shoppingCartProductId
     * @param userId
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/client/delete")
    public ModelAndView deleteProductPage(@RequestParam("productId") Integer shoppingCartProductId,@RequestParam("userId") Integer userId) throws Exception {
        shoppingCartProductService.deleteShoppingCartProduct(shoppingCartProductId);
        return new ModelAndView("redirect:/user/client/cart");
    }


    /**
     *
     * @param shoppingCartProductDTO
     * @param userId
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/client/update")
    public ModelAndView updateClientShoppingCartProduct(@Valid @ModelAttribute(name = "product") ShoppingCartProductDTO shoppingCartProductDTO, @RequestParam("userId") Integer userId) throws Exception {
        /*shoppingCartProductService.updateShoppingCartProduct(shoppingCartProductDTO);
        return new ModelAndView("redirect:/user/client/cart");*/

        ModelAndView modelAndView = new ModelAndView();
        try {
            shoppingCartProductService.updateShoppingCartProduct(shoppingCartProductDTO);
            modelAndView.setViewName("redirect:/user/client/cart");
            return modelAndView;
        } catch (Exception e) {
            modelAndView.setViewName("redirect:/user/client/cart");
            modelAndView.addObject("error", "Quantity exceeds stock");
            return modelAndView;
        }
    }
}
