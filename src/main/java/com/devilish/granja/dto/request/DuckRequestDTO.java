package com.devilish.granja.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DuckRequestDTO {

    @NotNull(message = "Nome não pode estar vázio!")
    private String name;
    @NotNull(message = "Id da mãe não pode ser nulo!")
    private Long maeId;
    @NotNull(message = "A venda não pode ser vázia!")
    private boolean sold;
    @NotNull(message = "O preço do pato não deve ser vázio!")
    private double price;

}
