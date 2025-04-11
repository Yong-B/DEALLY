package com.example.product.usecase;

import com.example.product.domain.Item;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemUpdateUseCase {
    Item update(Long itemId, Item updateParam);

}
