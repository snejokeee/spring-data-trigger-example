package dev.alubenets;

import dev.alubenets.persistance.OrderDetailsEntity;
import dev.alubenets.persistance.OrderDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsManagementService {

    private final OrderManagementService orderManagementService;
    private final OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsManagementService(OrderManagementService orderManagementService, OrderDetailsRepository orderDetailsRepository) {
        this.orderManagementService = orderManagementService;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public void addDetails(Long orderId, String article, Long quantity) {
        orderDetailsRepository.save(
            new OrderDetailsEntity()
                .setOrderId(orderId)
                .setArticle(article)
                .setQuantity(quantity)
        );
        orderManagementService.recalculateTotalPrice(orderId);
    }

    public void deleteDetails(Long orderId, String article) {
        orderDetailsRepository.findByOrderIdAndArticle(orderId, article)
            .ifPresent(orderDetailsRepository::delete);
        orderManagementService.recalculateTotalPrice(orderId);
    }

    public void changeDetails(Long orderId, String article, Long quantity) {
        var details = orderDetailsRepository.findByOrderIdAndArticle(orderId, article)
            .orElseThrow(() -> new RuntimeException("Order details not found"));
        details.setQuantity(quantity);
        orderDetailsRepository.save(details);
        orderManagementService.recalculateTotalPrice(orderId);
    }
}
