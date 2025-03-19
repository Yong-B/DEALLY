package com.example.product.domain;
import com.example.product.file.domain.UploadFile;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name")
    private String itemName;
    
    private Float price;
    
    private Integer quantity;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UploadFile> imagesFiles;
    
    
    @Builder(
            builderClassName = "UpdateItemBuilder",
            builderMethodName = "prepareUpdate",
            buildMethodName = "update"
    )
    public Item(String itemName, Float price, Integer quantity, Long userId) {
        
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Item updateFields(String itemName, Float price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        return this;
    }
    
    public void addImages(List<UploadFile> images) {
        this.imagesFiles = images;
    }
}
