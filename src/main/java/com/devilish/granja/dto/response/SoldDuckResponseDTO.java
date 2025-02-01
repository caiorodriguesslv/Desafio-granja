package com.devilish.granja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoldDuckResponseDTO {
    private List<DuckInfoDTO> ducks;
    private double totalValue;
    private ClientInfoDTO client;

    @Data
    @Builder
    public static class DuckInfoDTO {
        private String name;
        private double price;
        private String duckMother;
    }

    @Data
    @Builder
    public static class ClientInfoDTO {
        private String name;
    }
}