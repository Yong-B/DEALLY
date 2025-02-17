package com.example.product.domain;
import jakarta.persistence.*;
import lombok.*;
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
}
