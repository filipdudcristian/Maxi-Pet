package com.example.MaxiPet.validators;

import com.example.MaxiPet.constants.OrderConstants;
import com.example.MaxiPet.dto.OrderDTO;
import com.example.MaxiPet.entity.Order;
import com.example.MaxiPet.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class OrderValidator {

    private final OrderRepository orderRepository;
    private final UserValidator userValidator;

    public void validateOrderId(Integer orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            log.warn(OrderConstants.ORDER_NOT_FOUND, orderId);
            throw new Exception(OrderConstants.EXCEPTION_ORDER_NOT_FOUND);
        }
    }

    public void validateOrderDTOForUpdate(OrderDTO orderDTO) throws Exception {
        if (orderDTO == null) {
            log.warn(OrderConstants.DTO_EMPTY);
            throw new Exception(OrderConstants.EXCEPTION_DTO_NULL);
        }

        validateOrderId(orderDTO.getId());
        validateOrderFields(orderDTO);
    }

    public void validateOrderFields(OrderDTO orderDTO) throws Exception {
        if (orderDTO.getStatus() == null || orderDTO.getStatus().isEmpty()) {
            log.warn(OrderConstants.STATUS_EMPTY);
            throw new Exception(OrderConstants.EXCEPTION_STATUS_EMPTY);
        }
    }
}
