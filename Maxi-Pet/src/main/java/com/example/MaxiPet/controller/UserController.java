package com.example.MaxiPet.controller;

import com.example.MaxiPet.dto.OrderDTO;
import com.example.MaxiPet.dto.ShoppingCartProductDTO;
import com.example.MaxiPet.dto.UserDTO;
import com.example.MaxiPet.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
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
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param userDTO the dto of the user to be created
     * @return ResponseEntity containing the created UserDTO with HTTP status code 201 (Created) if successful.
     */
    @PostMapping
    public ResponseEntity<?> createUserPostman(@Valid @RequestBody UserDTO userDTO) throws Exception {

        UserDTO updatedUser = userService.createUser(userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);

    }


    /**
     * @param userDTO contains the data required to update the user information
     * @return ResponseEntity containing the updated UserDTO with HTTP status code 200 (OK) if successful
     * or HTTP status code 400 (Bad Request) otherwise.
     */
    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        UserDTO updatedUser = userService.updateUser(userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }


    /**
     * @param userId contains the id of the user to be retrieved
     * @return ResponseEntity containing the UserDTO with HTTP status code 200 (OK) if the user is found
     * or HTTP status code 400 (Bad Request) otherwise.
     */
    @SneakyThrows
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Integer userId) {
        UserDTO userDTO = userService.getUser(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);

    }

    /**
     *
     * @return
     */
   @GetMapping("/users/json")
    public ResponseEntity<?> getAllUser() {
        List<UserDTO> userDTO = userService.getAllUsers();
        return new ResponseEntity<>(userDTO, HttpStatus.OK);

    }

    /**
     * @param userId contains the id of the user to be deleted
     * @return ResponseEntity with HTTP status code 200 (OK) if the user is successfully deleted
     * or HTTP status code 400 (Bad Request) otherwise.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer userId) throws Exception {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/get")
    public ModelAndView getUserId(@RequestParam("id") Integer userId) throws Exception {
        ModelAndView modelAndView = new ModelAndView("/AdminPage/users");
        UserDTO user = userService.getUser(userId);
        modelAndView.addObject("users",user);
        return modelAndView;
    }

    /**
     *
     * @return
     */
    @GetMapping("/users")
    public ModelAndView adminPageUsers() {
        ModelAndView modelAndView = new ModelAndView("/AdminPage/users");
        List<UserDTO> users = userService.getAllUsers();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    /**
     *
     * @return
     */

    @GetMapping("/users/client")
    public ModelAndView clientPageUsers() {
        ModelAndView modelAndView = new ModelAndView("/ClientPage/clients");
        List<UserDTO> users = userService.getAllUsers();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/delete/{id}")
    public ModelAndView deleteUserPage(@PathVariable("id") Integer userId) throws Exception {
        userService.deleteUser(userId);
        return new ModelAndView("redirect:/user/users");
    }


    /**
     *
     * @param userDTO
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/update")
    public ModelAndView updateUserPage(@Valid @ModelAttribute(name = "user") UserDTO userDTO) throws Exception {
        userService.updateUser(userDTO);
        return new  ModelAndView("redirect:/user/users");
    }

    /**
     *
     * @param userDTO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/create")
    public ModelAndView createUserPage(@Valid @ModelAttribute("user") UserDTO userDTO) throws Exception {
        userService.createUser(userDTO);
        return new  ModelAndView("redirect:/user/users");
    }

    @GetMapping("/userCreate")
    public ModelAndView createUserPage(){return new ModelAndView("register");};
    @PostMapping( "/userCreate")
    public ModelAndView createUser(@ModelAttribute UserDTO userDTO) throws Exception {
        ModelAndView mav = new ModelAndView("home");
        UserDTO createdUser = userService.createUser(userDTO);

        //mav.addObject("user",userDTO);
        mav.addObject("userId", createdUser.getId());
        return mav;
    }


    /**
     *
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("/client/cart")
    public ModelAndView clientPageShoppingCartProducts(HttpSession session) throws Exception {
        ModelAndView modelAndView = new ModelAndView("/ClientPage/cart");
        Integer userId = ((UserDTO) session.getAttribute("loggedInUser")).getId();
        UserDTO user = userService.getUser(userId);
        List<ShoppingCartProductDTO> products = user.getShoppingCartDTO().getShoppingCartProductDTOList();

        // Check if the user has placed orders before
        boolean hasOrderedBefore = userService.hasOrderedBefore(userId);

        // Apply discount if the user has ordered before
        double totalPrice = user.getShoppingCartDTO().getTotalPrice();
        double discountAmount = 0.0;
        if (hasOrderedBefore) {
            // Apply a 10% discount for returning customers (adjust the discount rate as needed)
            double discountRate = 0.10; // 10% discount
            discountAmount = totalPrice * discountRate;
            totalPrice -= discountAmount;
        }

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalPrice", totalPrice);
        modelAndView.addObject("discountAmount", discountAmount);
        modelAndView.addObject("userId", userId);

        session.setAttribute("loggedInUser", session.getAttribute("loggedInUser"));

        return modelAndView;
    }


    /**
     *
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("/client/orders")
    public ModelAndView clientPageOrders(HttpSession session) throws Exception {
        ModelAndView modelAndView = new ModelAndView("/ClientPage/orders");
        Integer userId = ((UserDTO)session.getAttribute("loggedInUser")).getId();
        List<OrderDTO> orders = userService.getUser(userId).getOrderDTOList();
        modelAndView.addObject("orders", orders);
        session.setAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return modelAndView;
    }


    @Autowired
    private HttpSession session;
    @GetMapping("/login/client")
    public ModelAndView createLoginCustomerPage(){return  new ModelAndView("login");};
    @PostMapping("/login/client")
    public ModelAndView clientLogin(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UserDTO loggedInUser = userService.clientLogin(email,password);
            modelAndView.setViewName("redirect:/product/products/client");
            modelAndView.addObject("user", loggedInUser);
            session.setAttribute("loggedInUser", loggedInUser);
        } catch (IllegalArgumentException e) {
            modelAndView.setViewName("login");
            modelAndView.addObject("error", "Incorrect email or password");
        }
        return modelAndView;
    }
    @GetMapping("/login/admin")
    public ModelAndView createLoginAdminPage(){return new ModelAndView("loginAdmin");};
    @PostMapping("/login/admin")
    public ModelAndView adminLogin(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UserDTO loggedInUser = userService.adminLogin(email,password);
            modelAndView.setViewName("/AdminPage/users");
            modelAndView.addObject("user", loggedInUser);
            session.setAttribute("loggedInUser", loggedInUser);
        } catch (IllegalArgumentException e) {
            modelAndView.setViewName("loginAdmin");
            modelAndView.addObject("error", "Incorrect email or password");
        }
        return modelAndView;
    }
}
