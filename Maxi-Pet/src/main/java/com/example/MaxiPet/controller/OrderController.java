package com.example.MaxiPet.controller;

import com.example.MaxiPet.dto.OrderDTO;
import com.example.MaxiPet.dto.UserDTO;
import com.example.MaxiPet.service.OrderService;
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
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    /**
     *
     * @param orderId of the order of for which the order is created
     * @return ResponseEntity containing the created OrderDTO with HTTP status code 201 (Created) if successful.
     */
    @PostMapping(value = "/{id}")
    public ResponseEntity<?> createOrder(@PathVariable("id") Integer orderId) throws Exception {
        OrderDTO createdOrder = orderService.createOrder(orderId);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     *
     * @param orderDTO contains the data required to update the order information
     * @return ResponseEntity containing the updated OrderDTO with HTTP status code 200 (OK) if successful
     *         or HTTP status code 400 (Bad Request) otherwise.
     */
    @PutMapping
    public ResponseEntity<?> updateOrder(@Valid @RequestBody OrderDTO orderDTO) throws Exception {

        OrderDTO updatedOrder = orderService.updateOrder(orderDTO);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);

    }

    /**
     *
     * @param orderId contains the id of the order to be retrieved
     * @return ResponseEntity containing the OrderDTO with HTTP status code 200 (OK) if the order is found
     *         or HTTP status code 400 (Bad Request) otherwise.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") Integer orderId) throws Exception {
        OrderDTO orderDTO = orderService.getOrder(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/allOrders/{id}")
    public ResponseEntity<?> getAllOrdersForUser(@PathVariable("id") Integer userId) throws Exception {
        List<OrderDTO> orderDTOS = orderService.getAllOrdersForUser(userId);
        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);

    }

    /**
     *
     * @param orderId contains the id of the order to be deleted
     * @return ResponseEntity with HTTP status code 200 (OK) if the order is successfully deleted
     *          or HTTP status code 400 (Bad Request) otherwise.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Integer orderId) throws Exception {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/get")
    public ModelAndView getOrderId(@RequestParam("id") Integer orderId) throws Exception {
        ModelAndView modelAndView = new ModelAndView("/AdminPage/orders");
        OrderDTO order = orderService.getOrder(orderId);
        modelAndView.addObject("orders",order);
        return modelAndView;
    }

    /**
     *
     * @return
     */
    @GetMapping("/orders")
    public ModelAndView adminPageOrders() {
        ModelAndView modelAndView = new ModelAndView("/AdminPage/orders");
        List<OrderDTO> orders = orderService.getAllOrders();
        modelAndView.addObject("orders", orders);
        return modelAndView;
    }

    /**
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/delete/{id}")
    public ModelAndView deleteOrderPage(@PathVariable("id") Integer orderId) throws Exception {
        orderService.deleteOrder(orderId);
        return new ModelAndView("redirect:/order/orders");
    }

    /**
     *
     * @param orderDTO
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/update")
    public ModelAndView updateOrderPage(@Valid @ModelAttribute(name = "order") OrderDTO orderDTO) throws Exception {
        orderService.updateOrder(orderDTO);
        return new  ModelAndView("redirect:/order/orders");
    }

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/create")
    public ModelAndView createOrderPage(@Valid @ModelAttribute("userId") Integer userId) throws Exception {
        orderService.createOrder(userId);
        return new  ModelAndView("redirect:/order/orders");
    }

    /**
     *
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/client/order")
    public ModelAndView createOrderClient(@RequestParam("fileType") String fileType,HttpSession session) throws Exception {
        Integer userId = ((UserDTO)session.getAttribute("loggedInUser")).getId();
        orderService.createOrder(userId, fileType);
        session.setAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return new  ModelAndView("redirect:/product/products/client");
    }


}