package dev.alubenets;

import dev.alubenets.persistance.OrderDetailsEntity;
import dev.alubenets.persistance.OrderDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsManagementService {

    private final OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsManagementService(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public void addDetails(Long orderId, String article, Long quantity) {
        orderDetailsRepository.save(
            new OrderDetailsEntity()
                .setOrderId(orderId)
                .setArticle(article)
                .setQuantity(quantity)
        );
    }

    public void deleteDetails(Long orderId, String article) {
        orderDetailsRepository.findByOrderIdAndArticle(orderId, article)
            .ifPresent(orderDetailsRepository::delete);
    }

    public void changeDetails(Long orderId, String article, Long quantity) {
        var details = orderDetailsRepository.findByOrderIdAndArticle(orderId, article)
            .orElseThrow(() -> new RuntimeException("Order details not found"));
        details.setQuantity(quantity);
        orderDetailsRepository.save(details);
    }
}
