package com.devilish.granja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleResponseDTO {
    private Long id;
    private LocalDateTime dateSale;
    private Long clientId;
    private String clientName;
    private Long sellerId;
    private String sellerName;
    private List<Long> duckIds;
    private List<String> duckNames;
    private double totalValue;
}