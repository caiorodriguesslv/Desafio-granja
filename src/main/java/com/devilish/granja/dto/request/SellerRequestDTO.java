package com.devilish.granja.dto.request;

import com.devilish.granja.utils.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class SellerRequestDTO {

    @NotNull(message = "O cpf deve ter 11 digitos!")
    private String cpf;
    @NotNull(message = "O nome não pode estar vázio!")
    private String name;
    @NotNull(message = "A matricula não pode estar vazia e deve ter 6 digitos!")
    private String registration;


    public boolean isValidCpfAndRegistration() {
        boolean isValid = true;

        if (!Validator.isValidCpf(cpf)) {
            log.error("CPF inválido: {}", cpf);
            isValid = false;
        }


        if (!Validator.isValidRegistration(registration)) {
            log.error("Matrícula inválida: {}", registration);
            isValid = false;
        }

        return isValid;
    }

}
