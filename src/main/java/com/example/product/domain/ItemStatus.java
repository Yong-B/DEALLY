package com.example.product.domain;

public enum ItemStatus {
    AVAILABLE("판매중"),  // 판매중
    COMPLETED("거래완료");  // 거래완료

    private final String displayName;

    // 생성자
    ItemStatus(String displayName) {
        this.displayName = displayName;
    }

    // displayName을 반환하는 메서드
    public String getDisplayName() {
        return displayName;
    }
}
