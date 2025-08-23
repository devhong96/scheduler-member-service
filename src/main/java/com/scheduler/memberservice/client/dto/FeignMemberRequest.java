package com.scheduler.memberservice.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.EnumType.STRING;

public class FeignMemberRequest {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseExistenceResponse {

        private Boolean exists;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseReassignmentResponse {

        private Boolean exists;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateKakaoDirectOrderDto {

        private String studentId;

        private String username;

        private Integer quantity;

        @Enumerated(STRING)
        private OrderCategory orderCategory;

        private KakaoApproveOrderResponse response;
    }

    @Getter
    @Setter
    public static class KakaoApproveOrderResponse {

        private String aid;                 // 요청 고유 번호
        private String tid;                 // 결제 고유 번호
        private String cid;                 // 가맹점 코드
        private String partner_order_id;    // 가맹점 주문번호
        private String partner_user_id;     // 가맹점 회원 id
        private Amount amount;
        private CardInfo cardInfo;
        private String payment_method_type; // 결제 수단, CARD 또는 MONEY 중 하나
        private String item_name;           // 상품 이름
        private String item_code;           // 상품 코드
        private Integer quantity;           // 상품 수량
        private String created_at;          // 결제 준비 요청 시각
        private String approved_at;         // 결제 승인 시각
        private String payload;             // 결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
    }

    @Getter
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Amount {

        private Integer total;
        private Integer taxFree;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer greenDeposit;

    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CardInfo {

        private String kakaopayPurchaseCorp;          // 카카오페이 매입사명
        private String kakaopayPurchaseCorpCode;      // 카카오페이 매입사 코드
        private String kakaopayIssuerCorp;            // 카카오페이 발급사명
        private String kakaopayIssuerCorpCode;        // 카카오페이 발급사 코드
        private String bin;                           // 카드 BIN
        private String cardType;                      // 카드 타입
        private String installMonth;                  // 할부 개월 수
        private String approvedId;                    // 카드사 승인번호
        private String cardMid;                       // 카드사 가맹점 번호
        private String interestFreeInstall;           // 무이자할부 여부(Y/N)
        private String installmentType;               // 할부 유형 (24.02.01부터 제공)
        private String cardItemCode;                  // 카드 상품 코드

    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateNaverDirectOrderDto {

        private String studentId;

        private String username;

        private Integer quantity;

        @Enumerated(STRING)
        private OrderCategory orderCategory;

        private Detail detail;
    }

    @Getter
    @Setter
    public static class Detail {
        private String productName;
        private String merchantId;
        private String merchantName;
        private String cardNo;
        private String admissionYmdt;
        private String payHistId;
        private Integer totalPayAmount;
        private Integer applyPayAmount;
        private Integer primaryPayAmount;
        private Integer npointPayAmount;
        private Integer giftCardAmount;
        private Integer discountPayAmount;
        private Integer taxScopeAmount;
        private Integer taxExScopeAmount;
        private Integer environmentDepositAmount;
        private String primaryPayMeans;
        private String merchantPayKey;
        private String merchantUserKey;
        private String cardCorpCode;
        private String paymentId;
        private String admissionTypeCode;
        private Integer settleExpectAmount;
        private Integer payCommissionAmount;
        private String admissionState;
        private String tradeConfirmYmdt;
        private String cardAuthNo;
        private Integer cardInstCount;
        private Boolean usedCardPoint;
        private String bankCorpCode;
        private String bankAccountNo;
        private Boolean settleExpected;
        private Boolean extraDeduction;
        private String useCfmYmdt;
        private String userIdentifier;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateNicePayDirectOrderDto {

        private String studentId;

        private String username;

        private Integer quantity;

        @Enumerated(STRING)
        private OrderCategory orderCategory;

        private NicePayOrderResponse response;
    }

    @Getter
    @Setter
    public static class NicePayOrderResponse {

        private String resultCode;
        private String resultMsg;
        private String tid;
        private String cancelledTid;
        private String orderId;
        private String ediDate;
        private String signature;
        private String status;
        private String paidAt;
        private String failedAt;
        private String cancelledAt;
        private String payMethod;
        private int amount;
        private int balanceAmt;
        private String goodsName;
        private String mallReserved;
        private boolean useEscrow;
        private String currency;
        private String channel;
        private String approveNo;
        private String buyerName;
        private String buyerTel;
        private String buyerEmail;
        private String receiptUrl;
        private String mallUserId;
        private boolean issuedCashReceipt;
        private Object coupon;
        private Card card;
        private String vbank;
        private String cancels;
        private String cashReceipts;

        @Getter
        @Setter
        public static class Card {
            private String cardCode;
            private String cardName;
            private String cardNum;
            private int cardQuota;
            private boolean isInterestFree;
            private String cardType;
            private boolean canPartCancel;
            private String acquCardCode;
            private String acquCardName;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CancelOrderInfoResponse {

        @Enumerated(STRING)
        private Vendor vendor;
        private String vendorTid;
        private String productId;
        private String productName;
        private Integer cancelAmount;
    }

}
