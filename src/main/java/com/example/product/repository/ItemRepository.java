package com.example.product.repository;

import com.example.product.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findProductById(Long id);
    Optional<Item> findProductByUserId(String userId);

    @EntityGraph(attributePaths = {"imagesFiles"})
    @Query("SELECT i FROM Item i WHERE LOWER(i.itemName) LIKE LOWER(CONCAT('%', :searchKeyword, '%'))")
    Page<Item> findByItemNameContaining(String searchKeyword, Pageable pageable);
    
    @EntityGraph(attributePaths = {"imagesFiles"})
    Page<Item> findByUserId(String userId, Pageable pageable);

    Page<Item> findByBuyerId(String buyerId, Pageable pageable);

    
}

