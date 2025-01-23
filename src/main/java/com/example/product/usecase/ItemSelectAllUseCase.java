package com.example.product.usecase;

import com.example.product.domain.Item;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ItemSelectAllUseCase {
    List<Item> findAll();
    List<Item> findAll(Sort sort);
}
