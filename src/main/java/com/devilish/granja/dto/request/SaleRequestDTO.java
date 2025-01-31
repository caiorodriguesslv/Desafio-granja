package com.devilish.granja.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleRequestDTO {
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clientId;

    @NotNull(message = "O ID do vendedor é obrigatório")
    private Long sellerId;

    @NotEmpty(message = "A lista de IDs dos patos não pode estar vazia")
    private List<@NotNull(message = "O ID do pato não pode ser nulo") Long> duckIds;

    @NotNull(message = "O valor total da venda é obrigatório")
    private Double totalValue;
}
