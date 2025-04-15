package com.example.product.domain;
import com.example.product.file.domain.UploadFile;
import jakarta.annotation.Nullable;
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
    
    @Column(name = "user_id", nullable = false)
    private String userId;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UploadFile> imagesFiles;
    
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ItemStatus status = ItemStatus.AVAILABLE;

    @Nullable
    private String buyerId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Builder(
            builderClassName = "UpdateItemBuilder",
            builderMethodName = "prepareUpdate",
            buildMethodName = "update"
    )
    public Item(String itemName, Float price,  String userId, String description) {
        
        this.itemName = itemName;
        this.price = price;
        this.userId = userId;
        this.description = description;
    }

    public Item updateFields(String itemName, Float price,  String description) {
        this.itemName = itemName;
        this.price = price;
        this.description = description;
        return this;
    }
    
    public void addImages(List<UploadFile> images) {
        this.imagesFiles = images;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public void addBuyer(String buyerId) {
        this.buyerId = buyerId;
    }
    
}
