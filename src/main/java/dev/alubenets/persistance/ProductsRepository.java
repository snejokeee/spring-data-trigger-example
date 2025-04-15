package dev.alubenets.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductsRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAllByArticleIn(Collection<String> articles);
}
