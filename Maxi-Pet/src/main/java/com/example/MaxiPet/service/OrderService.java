package com.example.MaxiPet.service;

import com.example.MaxiPet.dto.Builders.OrderBuilder;
import com.example.MaxiPet.dto.EmailRequestDto;
import com.example.MaxiPet.dto.OrderDTO;
import com.example.MaxiPet.entity.*;
import com.example.MaxiPet.repository.*;
import com.example.MaxiPet.service.filegenerator.*;
import com.example.MaxiPet.config.RabbitSender;
import com.example.MaxiPet.validators.OrderValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final ProductRepository productRepository;
    private final OrderValidator orderValidator;
    private final RabbitSender rabbitSender;

    private final PdfFileGenerator pdfFileGenerator;
    private final CsvFileGenerator csvFileGenerator;

    private final TextFileGenerator textFileGenerator;


    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderProductRepository orderProductRepository, ShoppingCartRepository shoppingCartRepository, ShoppingCartProductRepository shoppingCartProductRepository, ProductRepository productRepository, OrderValidator orderValidator, RabbitSender rabbitSender, PdfFileGenerator pdfFileGenerator, CsvFileGenerator csvFileGenerator, TextFileGenerator textFileGenerator)
    {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderProductRepository = orderProductRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartProductRepository = shoppingCartProductRepository;
        this.productRepository = productRepository;
        this.orderValidator = orderValidator;
        this.rabbitSender = rabbitSender;
        this.pdfFileGenerator = pdfFileGenerator;
        this.csvFileGenerator = csvFileGenerator;
        this.textFileGenerator = textFileGenerator;
    }

    public OrderDTO createOrder(Integer userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            log.warn("The user with id {} was not found in the DB!", userId);
            throw new Exception("User not found");
        }

        User user = optionalUser.get();
        ShoppingCart shoppingCart = user.getShoppingCart();
        List<ShoppingCartProduct> shoppingCartProductList = user.getShoppingCart().getShoppingCartProductList();

        for (ShoppingCartProduct shoppingCartProduct : shoppingCartProductList) {
            if (shoppingCartProduct.getProduct().getStock() < shoppingCartProduct.getQuantity()) {
                log.warn("The product {} with {} items in stock doesn't have the {} items requested!", shoppingCartProduct.getProduct().getName(), shoppingCartProduct.getProduct().getStock(), shoppingCartProduct.getQuantity());
                throw new Exception("The product " + shoppingCartProduct.getProduct().getName() + " with " + shoppingCartProduct.getProduct().getStock() + " items in stock doesn't have the " + shoppingCartProduct.getQuantity() + " items requested!");
            }
        }

        Order order = Order.builder()
                .orderDate(LocalDate.now())
                .status("Processing")
                .user(user)
                .build();

        order = orderRepository.save(order);

        Order finalOrder = order;
        List<OrderProduct> orderProductList = shoppingCartProductList.stream().map(s -> OrderProduct.builder()
                .product(s.getProduct())
                .productId(s.getProduct().getId())
                .order(finalOrder)
                .orderId(finalOrder.getId())
                .quantity(s.getQuantity())
                .build()).toList();
        orderProductList = (List<OrderProduct>) orderProductRepository.saveAll(orderProductList);

        order.setTotalPrice(shoppingCart.getTotalPrice());
        order = orderRepository.save(order);

        updateStockForProductsInOrder(orderProductList);
        deleteShoppingCartItems(shoppingCart.getId());

        order.setOrderProductList(orderProductList);

        //email
        EmailRequestDto emailRequest = new EmailRequestDto();
        emailRequest.setId(user.getId());
        emailRequest.setFirstName(user.getFirstName());
        emailRequest.setLastName(user.getName());
        emailRequest.setRecipientEmail(user.getEmail()); // Assuming email is stored in UserDTO
        emailRequest.setSubject("Order Confirmation"); // Set your subject here


        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<h1>Dear ").append(user.getFirstName()).append(",</h1><br>");
        bodyBuilder.append("<h1>Your order:</h1><br>");
        // bodyBuilder.append("<p>Your order details:</p><br>");

        // Iterate through each OrderItem and append its details to the email body
        for (OrderProduct orderProduct : orderProductList) {
            bodyBuilder.append("<p>Product: ").append(orderProduct.getProduct().getName()).append("</p>");
            bodyBuilder.append("<p>Quantity: ").append(orderProduct.getQuantity()).append("</p>");
            bodyBuilder.append("<p>Price: ").append(orderProduct.getProduct().getDiscountedPrice()).append("*")
                    .append(orderProduct.getQuantity()).append(": ")
                    .append(orderProduct.getProduct().getDiscountedPrice()*orderProduct.getQuantity()).append(" RON</p>");

            //bodyBuilder.append("<br>");
        }
        bodyBuilder.append("<h2><b>Total: ").append(order.getTotalPrice()).append("<b></h2>");
        bodyBuilder.append("<p style='text-align: right;'>Date: ").append(order.getOrderDate()).append("</p");
        emailRequest.setBody(bodyBuilder.toString());
        // Send the email request to the email service
        rabbitSender.send(emailRequest);

        return OrderBuilder.toOrderDTO(order);

    }

    /***
     *
     * @param userId is the user for which the order is created
     * @return the userDTO of the created order entity
     */
    public OrderDTO createOrder(Integer userId, String fileType) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            log.warn("The user with id {} was not found in the DB!", userId);
            throw new Exception("User not found");
        }

        User user = optionalUser.get();
        ShoppingCart shoppingCart = user.getShoppingCart();
        List<ShoppingCartProduct> shoppingCartProductList = user.getShoppingCart().getShoppingCartProductList();

        for (ShoppingCartProduct shoppingCartProduct : shoppingCartProductList) {
            if (shoppingCartProduct.getProduct().getStock() < shoppingCartProduct.getQuantity()) {
                log.warn("The product {} with {} items in stock doesn't have the {} items requested!", shoppingCartProduct.getProduct().getName(), shoppingCartProduct.getProduct().getStock(), shoppingCartProduct.getQuantity());
                throw new Exception("The product " + shoppingCartProduct.getProduct().getName() + " with " + shoppingCartProduct.getProduct().getStock() + " items in stock doesn't have the " + shoppingCartProduct.getQuantity() + " items requested!");
            }
        }

        boolean hasOrderedBefore;
        List<Order> userOrders = orderRepository.findOrdersByUserId(userId);
        hasOrderedBefore = !userOrders.isEmpty();


        float totalPriceInitial = shoppingCart.getTotalPrice();
        float totalPrice;

        if (hasOrderedBefore)
            totalPrice = totalPriceInitial * (1 - 0.1F);
        else
            totalPrice = totalPriceInitial;


        Order order = Order.builder()
                .orderDate(LocalDate.now())
                .status("Processing")
                .user(user)
                .build();

        order = orderRepository.save(order);

        Order finalOrder = order;
        List<OrderProduct> orderProductList = shoppingCartProductList.stream().map(s -> OrderProduct.builder()
                .product(s.getProduct())
                .productId(s.getProduct().getId())
                .order(finalOrder)
                .orderId(finalOrder.getId())
                .quantity(s.getQuantity())
                .build()).toList();
        orderProductList = (List<OrderProduct>) orderProductRepository.saveAll(orderProductList);

        //order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setTotalPrice(totalPrice);
        order = orderRepository.save(order);

        updateStockForProductsInOrder(orderProductList);
        deleteShoppingCartItems(shoppingCart.getId());

        order.setOrderProductList(orderProductList);

//        if(fileType.equals("text"))
//            textFileGenerator.generateFile(order);
//        if(fileType.equals("pdf"))
//            pdfFileGenerator.generateFile(order);
//        if(fileType.equals("csv"))
//            csvFileGenerator.generateFile(order);

        FileGenerator fileGenerator = FileGeneratorFactory.getFileGenerator(fileType);

        fileGenerator.generateFile(order);

        //email
        EmailRequestDto emailRequest = new EmailRequestDto();
        emailRequest.setId(user.getId());
        emailRequest.setFirstName(user.getFirstName());
        emailRequest.setLastName(user.getName());
        emailRequest.setRecipientEmail(user.getEmail()); // Assuming email is stored in UserDTO
        emailRequest.setSubject("Order Confirmation"); // Set your subject here

        emailRequest.setFileType(fileType);

        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<h1>Dear ").append(user.getFirstName()).append(",</h1><br>");
        bodyBuilder.append("<h1>Your order:</h1><br>");
       // bodyBuilder.append("<p>Your order details:</p><br>");

        // Iterate through each OrderItem and append its details to the email body
        for (OrderProduct orderProduct : orderProductList) {
            bodyBuilder.append("<p>Product: ").append(orderProduct.getProduct().getName()).append("</p>");
            bodyBuilder.append("<p>Quantity: ").append(orderProduct.getQuantity()).append("</p>");
            bodyBuilder.append("<p>Price: ").append(orderProduct.getProduct().getDiscountedPrice()).append("*")
                    .append(orderProduct.getQuantity()).append(": ")
                    .append(orderProduct.getProduct().getDiscountedPrice()*orderProduct.getQuantity()).append(" RON</p>");

            //bodyBuilder.append("<br>");
        }
        if(hasOrderedBefore)
            bodyBuilder.append("<h2><b>Total: ").append(totalPriceInitial).append(" - ").append(totalPriceInitial*0.1).append(" = ").append(totalPrice).append("<b></h2>");
        else
            bodyBuilder.append("<h2><b>Total: ").append(order.getTotalPrice()).append("<b></h2>");
        bodyBuilder.append("<p style='text-align: right;'>Date: ").append(order.getOrderDate()).append("</p");
        emailRequest.setBody(bodyBuilder.toString());
        // Send the email request to the email service
        rabbitSender.send(emailRequest);

        return OrderBuilder.toOrderDTO(order);

    }

    /***
     *
     * @param orderDTO contains the data of the user to be updated
     * @return the userDTO of the updated user entity
     * @throws Exception if the user is not found
     */
    public OrderDTO updateOrder(OrderDTO orderDTO) throws Exception {
        orderValidator.validateOrderDTOForUpdate(orderDTO);

        Optional<Order> optionalOrder = orderRepository.findById(orderDTO.getId());

        Order order = optionalOrder.get();

        order.setStatus(orderDTO.getStatus());

        Order updatedOrder = orderRepository.save(order);

        return OrderBuilder.toOrderDTO(updatedOrder);
    }

    /***
     *
     * @param orderId contains the id of the user to be retrieved
     * @return the userDTO of the retrieved user entity
     * @throws Exception if the user is not found
     */
    public OrderDTO getOrder(Integer orderId) throws Exception {
        orderValidator.validateOrderId(orderId);
        Optional<Order> order = orderRepository.findById(orderId);

        return OrderBuilder.toOrderDTO(order.get());
    }

    /**
     *
     * @return
     */
    public List<OrderDTO> getAllOrders() {
        List<Order> orderList = (List<Order>) orderRepository.findAll();
        return orderList.stream()
                .map(OrderBuilder::toOrderDTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public List<OrderDTO> getAllOrdersForUser(Integer userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            log.warn("The user with id {} was not found in the DB!", userId);
            throw new Exception("User not found");
        }
        List<Order> orderList = orderRepository.getAllByUser(user.get());
        return orderList.stream()
                .map(OrderBuilder::toOrderDTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param orderId the id of the user to be deleted
     * @throws Exception if the user is not found
     */
    public void deleteOrder(Integer orderId) throws Exception {
        orderValidator.validateOrderId(orderId);

        Optional<Order> order = orderRepository.findById(orderId);
        orderRepository.deleteById(orderId);
    }

    /**
     *
     * @param orderProductList
     */
    private void updateStockForProductsInOrder(List<OrderProduct> orderProductList){
        List<Product> productList = orderProductList.stream().map(o -> {
            Product p = o.getProduct();
            p.setStock(p.getStock() - o.getQuantity());
            return p;
        }).toList();

        productRepository.saveAll(productList);
    }

    public Float getTotalBasePrice(Integer shoppingCartId) throws Exception {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if(optionalShoppingCart.isEmpty()) {
            log.warn("The shopping cart with id {} was not found in the DB!", shoppingCartId);
            throw new Exception("Shopping cart not found");
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        if (shoppingCart.getShoppingCartProductList().isEmpty()) {
            return (float) 0.0;
        }

        return shoppingCart.getShoppingCartProductList().stream().collect(Collectors.summingDouble(s -> s.getProduct().getBasePrice() * s.getQuantity())).floatValue();
    }

    public Float getTotalDiscountedPrice(Integer shoppingCartId) throws Exception {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if(optionalShoppingCart.isEmpty()) {
            log.warn("The shopping cart with id {} was not found in the DB!", shoppingCartId);
            throw new Exception("Shopping cart not found");
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        if (shoppingCart.getShoppingCartProductList().isEmpty()) {
            return (float) 0.0;
        }
        return shoppingCart.getShoppingCartProductList().stream().collect(Collectors.summingDouble(s -> s.getProduct().getDiscountedPrice() * s.getQuantity())).floatValue();
    }

    /**
     *
     * @param shoppingCartId
     * @throws Exception
     */
    public void deleteShoppingCartItems(Integer shoppingCartId) throws Exception {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if(optionalShoppingCart.isEmpty()) {
            log.warn("The shopping cart with id {} was not found in the DB!", shoppingCartId);
            throw new Exception("Shopping cart not found");
        }

        ShoppingCart shoppingCart = optionalShoppingCart.get();
        shoppingCart.getShoppingCartProductList().forEach(s -> shoppingCartProductRepository.deleteById(s.getId()));
        shoppingCart.setShoppingCartProductList(Collections.emptyList());
        shoppingCart.setTotalPrice(Float.parseFloat("0.0"));
        shoppingCartRepository.save(shoppingCart);
    }
}
