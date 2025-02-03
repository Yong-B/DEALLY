package com.example.product.controller.dto;

import com.example.product.domain.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public  final class ItemCommandDto {
    private ItemCommandDto() {
        
    }

    @Builder
    public record ItemSaveRequest(
            @NotBlank(message = "상품명을 입력하십시오.") // null, empty "", blank " "
            @Size(min = 3, message = "상품명은 세 글자 이상 입력하세요.")
            @Size(max = 50, message = "상품명은 최대 50글자입니다.")
            String itemName,

            @NotNull(message = "상품 가격을 입력하십시오.")
            Integer price,
            @NotNull(message = "상품 수량을 입력하십시오.")
            Integer quantity) {
    }
}
