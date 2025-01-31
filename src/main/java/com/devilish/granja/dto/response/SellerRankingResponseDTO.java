package com.devilish.granja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerRankingResponseDTO {
    private Long sellerId;
    private String sellerName;
    private int totalSales;
}
