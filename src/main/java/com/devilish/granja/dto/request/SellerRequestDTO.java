package com.devilish.granja.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerRequestDTO {

    @NotNull(message = "O cpf deve ter 11 digitos!")
    private String cpf;
    @NotNull(message = "O nome não pode estar vázio!")
    private String name;
    @NotNull(message = "A matricula não pode estar vazia e deve ter 6 digitos!")
    private String registration;

}
