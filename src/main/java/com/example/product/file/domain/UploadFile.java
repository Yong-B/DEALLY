package com.example.product.file.domain;

import com.example.product.domain.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String uploadFileName;
    
    private String storeFileName;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ 여러 개의 파일이 하나의 Item과 연결됨 (N:1 관계)
    @JoinColumn(name = "item_id", nullable = false)  // ✅ 외래키 컬럼 이름 지정
    private Item item;

    public UploadFile(String uploadFileName, String storeFileName, Item item) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.item = item;
    }

}
