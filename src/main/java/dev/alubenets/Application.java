package dev.alubenets;

import dev.alubenets.persistance.ProductEntity;
import dev.alubenets.persistance.ProductsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CommandLineRunner initProducts(
        ProductsRepository productsRepository
    ) {
        return args -> productsRepository.saveAll(
            List.of(
                new ProductEntity().setArticle("product_1").setPrice(10.10),
                new ProductEntity().setArticle("product_2").setPrice(20.20),
                new ProductEntity().setArticle("product_3").setPrice(30.30),
                new ProductEntity().setArticle("product_4").setPrice(40.40)
            )
        );
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    CommandLineRunner createTestOrder(
        OrderManagementService service
    ) {
        return args -> service.createOrder(
            Map.of(
                "product_1", 1L,
                "product_2", 2L,
                "product_3", 3L
            )
        );
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    CommandLineRunner findTestOrder(
        OrderManagementService service
    ) {
        return args -> service.findOrderById(1L);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 3)
    CommandLineRunner updateTestOrder(
        OrderDetailsManagementService service
    ) {
        return args -> {
            service.addDetails(1L, "product_4", 4L);
            service.deleteDetails(1L, "product_3");
            service.changeDetails(1L, "product_2", 1L);

            // finally we should have order with:
            // product_4 * 4
            // product_3 * 2
            // product_2 * 1
            // product_1 * 1
        };
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 4)
    CommandLineRunner findTestOrderWithWrongTotalPrice(
        OrderManagementService service
    ) {
        return args -> service.findOrderById(1L);
    }
}
