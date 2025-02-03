package com.example.product.repository;

import com.example.product.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findProductById(Long id);
}

