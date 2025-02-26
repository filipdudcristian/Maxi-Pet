package com.example.MaxiPet.service;

import com.example.MaxiPet.dto.Builders.UserBuilder;
import com.example.MaxiPet.dto.UserDTO;
import com.example.MaxiPet.entity.EmailRequest;
import com.example.MaxiPet.entity.Order;
import com.example.MaxiPet.entity.ShoppingCart;
import com.example.MaxiPet.entity.User;
import com.example.MaxiPet.repository.OrderRepository;
import com.example.MaxiPet.repository.ShoppingCartRepository;
import com.example.MaxiPet.repository.UserRepository;
import com.example.MaxiPet.config.RestService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.MaxiPet.validators.UserValidator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final UserValidator userValidator;
    private final RestService restService;

    @Autowired
    public UserService(UserRepository userRepository, ShoppingCartRepository shoppingCartRepository, OrderRepository orderRepository, UserValidator userValidator, RestService restService) {
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.userValidator = userValidator;
        this.restService = restService;
    }

    /***
     *
     * @param userDTO contains the data of the user to be created
     * @return the userDTO of the created user entity
     */
    public UserDTO createUser(UserDTO userDTO) throws Exception {
        userValidator.validateUserFields(userDTO);
        User newUser = UserBuilder.toEntity(userDTO);
        ShoppingCart shoppingCart = shoppingCartRepository.save(ShoppingCart.builder().totalPrice(Float.parseFloat("0.0")).build());
        newUser.setShoppingCart(shoppingCart);
        User user = userRepository.save(newUser);

        shoppingCart.setUser(user);
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        user.setShoppingCart(shoppingCart);



        //email
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setId(user.getId());
        emailRequest.setFirstName(userDTO.getFirstName());
        emailRequest.setLastName(userDTO.getName());
        emailRequest.setRecipientEmail(userDTO.getEmail()); // Assuming email is stored in UserDTO
        emailRequest.setSubject("Welcome to Our Platform"); // Set your subject here
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<h1>Dear ").append(userDTO.getFirstName()).append(",</h1><br>");
        bodyBuilder.append("<h1>Welcome to our platform!</h1><br>");
        emailRequest.setBody(bodyBuilder.toString());
        // Send the email request to the email service
        restService.sendEmailToMailApp(emailRequest);

        return UserBuilder.toUserDTO(user);
    }

    /***
     *
     * @param userDTO contains the data of the user to be updated
     * @return the userDTO of the updated user entity
     * @throws Exception if the user is not found
     */
    public UserDTO updateUser(UserDTO userDTO) throws Exception {
        userValidator.validateUserDTOForUpdate(userDTO);

        Optional<User> optionalUser = userRepository.findById(userDTO.getId());

        User user = optionalUser.get();

        user.setRole(userDTO.getRole());
        user.setName(userDTO.getName());
        user.setFirstName(userDTO.getFirstName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());

        User updatedUser = userRepository.save(user);

        return UserBuilder.toUserDTO(updatedUser);
    }


    /***
     *
     * @param userId contains the id of the user to be retrieved
     * @return the userDTO of the retrieved user entity
     * @throws Exception if the user is not found
     */
    public UserDTO getUser(Integer userId) throws Exception {
            userValidator.validateUserId(userId);
            Optional<User> user = userRepository.findById(userId);
            return UserBuilder.toUserDTO(user.get());

    }

    /**
     *
     * @return
     */
    public List<UserDTO> getAllUsers() {
        List<User> personList = (List<User>) userRepository.findAll();
        return personList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * @param userId the id of the user to be deleted
     * @throws Exception if the user is not found
     */
    public void deleteUser(Integer userId) throws Exception {
        userValidator.validateUserId(userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("The user with id {} was not found in the DB!", userId);
            throw new Exception("User not found");
        }
        userRepository.deleteById(userId);
    }


    /**
     * login part
     */
    public UserDTO clientLogin(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email,password);

        if (user != null && user.getRole().equals("ROLE_CLIENT")) {
            storeUserInSession(user);
            return UserBuilder.toUserDTO(user);
        } else {
            log.warn("Client login failed: Incorrect email or password");
            throw new IllegalArgumentException("Incorrect email or password");
        }
    }

    public UserDTO adminLogin(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user != null && user.getRole().equals("ROLE_ADMIN")) {
            storeUserInSession(user);
            return UserBuilder.toUserDTO(user);
        } else {
            log.warn("Admin login failed: Incorrect email or password");
            throw new IllegalArgumentException("Incorrect email or password");
        }
    }

    private void storeUserInSession(User user) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        session.setAttribute("loggedInUser", user);
    }

    public boolean hasOrderedBefore(int userId) {
        // Assuming you have a method in your UserRepository to retrieve user orders by userId
        List<Order> userOrders = orderRepository.findOrdersByUserId(userId);

        // Check if the user has any orders
        return !userOrders.isEmpty();
    }
}
