package dev.alubenets;

import dev.alubenets.persistance.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderManagementService {

    private static final Logger log = LoggerFactory.getLogger(OrderManagementService.class);

    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public OrderManagementService(
        OrdersRepository ordersRepository,
        ProductsRepository productsRepository,
        OrderDetailsRepository orderDetailsRepository
    ) {
        this.ordersRepository = ordersRepository;
        this.productsRepository = productsRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    /**
     * @param orderRequest map of article to quantity
     */
    @Transactional
    public void createOrder(Map<String, Long> orderRequest) {
        var products = productsRepository.findAllByArticleIn(orderRequest.keySet());
        var orderItems = products.stream()
            .map(p -> {
                var quantity = orderRequest.get(p.getArticle());
                var orderDetails = new OrderDetailsEntity();
                orderDetails.setQuantity(quantity);
                orderDetails.setArticle(p.getArticle());
                return orderDetails;
            })
            .toList();

        var totalOrderPrice = products.stream()
            .map(p -> {
                var price = p.getPrice();
                return orderRequest.get(p.getArticle()) * price;
            })
            .reduce(Double::sum)
            .orElse(0.0);

        var newOrder = new OrderEntity();
        newOrder.setTotalPrice(totalOrderPrice);

        var order = ordersRepository.save(newOrder);
        orderItems.forEach(o -> o.setOrderId(order.getOrderId()));
        orderDetailsRepository.saveAll(orderItems);
    }

    public void findOrderById(Long orderId) {
        var order = ordersRepository.findById(orderId).orElseThrow();
        var items = orderDetailsRepository.findALlByOrderId(orderId);
        var orderDetails = items.stream()
            .collect(Collectors.toMap(OrderDetailsEntity::getArticle, OrderDetailsEntity::getQuantity));

        log.info("Found order with id: " + orderId + " for total price: {}", order.getTotalPrice());
        log.info("Order Details: " + orderDetails);
    }
}
