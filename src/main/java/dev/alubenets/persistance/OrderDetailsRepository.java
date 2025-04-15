package dev.alubenets.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Long> {
    List<OrderDetailsEntity> findALlByOrderId(Long orderId);

    Optional<OrderDetailsEntity> findByOrderIdAndArticle(Long orderId, String article);
}
