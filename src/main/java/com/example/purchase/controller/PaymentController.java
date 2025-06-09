package com.example.purchase.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private IamportClient iamportClient;

    @Value("${iamport.api-key}")
    private String apiKey;

    @Value("${iamport.api-secret}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }
    
    @PostMapping("/payment/validation/{imp_uid}")
    @ResponseBody
    public ResponseEntity<String> validateIamport(@PathVariable String imp_uid, @RequestBody Map<String, Object> paymentData) {
        try {
            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
            log.info("결제 요청 응답. 주문 번호: {}", payment.getResponse().getMerchantUid());

            // 추가 검증 로직 가능 (금액 비교 등)
            // 예: payment.getResponse().getAmount() 와 paymentData.get("amount") 비교

            return ResponseEntity.ok("결제 검증 및 처리 성공");
        } catch (Exception e) {
            log.error("결제 검증 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 검증 실패");
        }
    }
}