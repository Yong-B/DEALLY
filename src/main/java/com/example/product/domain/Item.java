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
    
    private Integer price;
    
    private Integer quantity;

    @Builder(
            builderClassName = "UpdateItemBuilder",
            builderMethodName = "prepareUpdate",
            buildMethodName = "update"
    )
    public Item(String itemName, Integer price, Integer quantity) {
        
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public Item updateFields(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        return this;
    }
}
