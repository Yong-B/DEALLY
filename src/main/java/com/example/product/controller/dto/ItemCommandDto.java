package com.example.product.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public  final class ItemCommandDto {
    private ItemCommandDto() {

    }

    @Builder
    public record ItemSaveRequest(
            @NotBlank(message = "상품명을 입력하십시오.") // null, empty "", blank " "
            @Size(min = 3, message = "상품명은 세 글자 이상 입력하세요.")
            @Size(max = 50, message = "상품명은 최대 50글자입니다.")
            String itemName,

            @Min(0)
            @NotNull(message = "상품 가격을 입력하십시오.")
            Float price,
            
            @NotBlank(message = "상품 상세 설명을 입력하십시오.")
            @Size(max = 500, message = "상품 상세 설명은 최대 500글자입니다.")
            String description,  // 상품 상세 설명 필드 추가
            
            List<MultipartFile> imageFiles
    ) {
    }
}