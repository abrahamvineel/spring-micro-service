package com.pro.orderservice.service;

import com.pro.orderservice.dto.OrderLineItemsDto;
import com.pro.orderservice.dto.OrderRequest;
import com.pro.orderservice.model.Order;
import com.pro.orderservice.model.OrderLineItems;
import com.pro.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        //call inventory service and place order if product is in stock
        Boolean result = webClient.get()
                .uri("http://localhost:9192/api/inventory")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(result) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is in stock, please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        return orderLineItems;
    }
}
