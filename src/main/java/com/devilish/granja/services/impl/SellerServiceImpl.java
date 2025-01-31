package com.devilish.granja.services.impl;

import com.devilish.granja.dto.request.SellerRequestDTO;
import com.devilish.granja.dto.response.SellerResponseDTO;
import com.devilish.granja.entities.Seller;
import com.devilish.granja.repository.SellerRepository;
import com.devilish.granja.services.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    @Override
    public SellerResponseDTO save(SellerRequestDTO sellerRequestDTO) {
        log.info("Iniciando método save para o vendedor: {}", sellerRequestDTO.getName());


        if (!CpfValidator.isValid(sellerRequestDTO.getCpf())) {
            log.error("CPF inválido: {}", sellerRequestDTO.getCpf());
            throw new RuntimeException("CPF inválido.");
        }


        if (!MatriculaValidator.isValid(sellerRequestDTO.getRegistration())) {
            log.error("Matrícula inválida: {}", sellerRequestDTO.getRegistration());
            throw new RuntimeException("Matrícula inválida.");
        }


        sellerRepository.findByCpf(sellerRequestDTO.getCpf()).ifPresent(seller -> {
            log.error("Já existe um vendedor com o CPF: {}", sellerRequestDTO.getCpf());
            throw new RuntimeException("Já existe um vendedor com o CPF informado.");
        });


        sellerRepository.findByRegistration(sellerRequestDTO.getRegistration()).ifPresent(seller -> {
            log.error("Já existe um vendedor com a matrícula: {}", sellerRequestDTO.getRegistration());
            throw new RuntimeException("Já existe um vendedor com a matrícula informada.");
        });


        Seller seller = Seller.builder()
                .name(sellerRequestDTO.getName())
                .cpf(sellerRequestDTO.getCpf())
                .registration(sellerRequestDTO.getRegistration())
                .build();

        Seller savedSeller = sellerRepository.save(seller);

        log.info("Vendedor salvo com sucesso: ID={}, Nome={}, CPF={}, Matrícula={}",
                savedSeller.getId(), savedSeller.getName(), savedSeller.getCpf(), savedSeller.getRegistration());

        return SellerResponseDTO.builder()
                .id(savedSeller.getId())
                .name(savedSeller.getName())
                .cpf(savedSeller.getCpf())
                .registration(savedSeller.getRegistration())
                .build();
    }

    @Override
    public SellerResponseDTO update(Long id, SellerRequestDTO sellerRequestDTO) {
        log.info("Atualizando vendedor com ID: {}", id);

        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Vendedor não encontrado com o ID: {}", id);
                    return new RuntimeException("Vendedor não encontrado: " + id);
                });


        if (!CpfValidator.isValid(sellerRequestDTO.getCpf())) {
            log.error("CPF inválido: {}", sellerRequestDTO.getCpf());
            throw new RuntimeException("CPF inválido.");
        }


        if (!MatriculaValidator.isValid(sellerRequestDTO.getRegistration())) {
            log.error("Matrícula inválida: {}", sellerRequestDTO.getRegistration());
            throw new RuntimeException("Matrícula inválida.");
        }


        if (!seller.getCpf().equals(sellerRequestDTO.getCpf())) {
            sellerRepository.findByCpf(sellerRequestDTO.getCpf()).ifPresent(existingSeller -> {
                log.error("Já existe um vendedor com o CPF: {}", sellerRequestDTO.getCpf());
                throw new RuntimeException("Já existe um vendedor com o CPF informado.");
            });
        }


        if (!seller.getRegistration().equals(sellerRequestDTO.getRegistration())) {
            sellerRepository.findByRegistration(sellerRequestDTO.getRegistration()).ifPresent(existingSeller -> {
                log.error("Já existe um vendedor com a matrícula: {}", sellerRequestDTO.getRegistration());
                throw new RuntimeException("Já existe um vendedor com a matrícula informada.");
            });
        }


        seller.setName(sellerRequestDTO.getName());
        seller.setCpf(sellerRequestDTO.getCpf());
        seller.setRegistration(sellerRequestDTO.getRegistration());

        Seller updatedSeller = sellerRepository.save(seller);

        log.info("Vendedor atualizado com sucesso: ID={}, Nome={}, CPF={}, Matrícula={}",
                updatedSeller.getId(), updatedSeller.getName(), updatedSeller.getCpf(), updatedSeller.getRegistration());

        return SellerResponseDTO.builder()
                .id(updatedSeller.getId())
                .name(updatedSeller.getName())
                .cpf(updatedSeller.getCpf())
                .registration(updatedSeller.getRegistration())
                .build();
    }

    @Override
    public SellerResponseDTO findById(Long id) {
        log.info("Buscando vendedor com ID: {}", id);

        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Vendedor não encontrado com o ID: {}", id);
                    return new RuntimeException("Vendedor não encontrado: " + id);
                });

        log.info("Vendedor encontrado: ID={}, Nome={}, CPF={}, Matrícula={}",
                seller.getId(), seller.getName(), seller.getCpf(), seller.getRegistration());

        return SellerResponseDTO.builder()
                .id(seller.getId())
                .name(seller.getName())
                .cpf(seller.getCpf())
                .registration(seller.getRegistration())
                .build();
    }

    @Override
    public List<SellerResponseDTO> findAll() {
        log.info("Listando todos os vendedores");

        List<Seller> sellers = sellerRepository.findAll();

        log.info("Total de vendedores encontrados: {}", sellers.size());

        return sellers.stream()
                .map(seller -> {
                    log.debug("Convertendo vendedor para DTO: ID={}, Nome={}, CPF={}, Matrícula={}",
                            seller.getId(), seller.getName(), seller.getCpf(), seller.getRegistration());
                    return SellerResponseDTO.builder()
                            .id(seller.getId())
                            .name(seller.getName())
                            .cpf(seller.getCpf())
                            .registration(seller.getRegistration())
                            .build();
                })
                .collect(Collectors.toList());
    }


    private static class CpfValidator {
        public static boolean isValid(String cpf) {
            // Remove caracteres não numéricos
            cpf = cpf.replaceAll("[^0-9]", "");

            // Verifica se o CPF tem 11 dígitos
            if (cpf.length() != 11) {
                return false;
            }

            // Verifica se todos os dígitos são iguais (CPF inválido)
            if (cpf.matches("(\\d)\\1{10}")) {
                return false;
            }

            // Calcula o primeiro dígito verificador
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Integer.parseInt(cpf.substring(i, i + 1)) * (10 - i);
            }
            int remainder = sum % 11;
            int digit1 = (remainder < 2) ? 0 : 11 - remainder;

            // Calcula o segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Integer.parseInt(cpf.substring(i, i + 1)) * (11 - i);
            }
            remainder = sum % 11;
            int digit2 = (remainder < 2) ? 0 : 11 - remainder;

            // Verifica se os dígitos calculados são iguais aos dígitos do CPF
            return cpf.substring(9).equals(String.valueOf(digit1) + String.valueOf(digit2));
        }

        private static int calculateDigit(String str, int[] weights) {
            int sum = 0;
            for (int i = 0; i < str.length(); i++) {
                sum += Integer.parseInt(str.substring(i, i + 1)) * weights[i];
            }
            int remainder = sum % 11;
            return remainder < 2 ? 0 : 11 - remainder;
        }
    }


    private static class MatriculaValidator {
        public static boolean isValid(String matricula) {

            matricula = matricula.replaceAll("[^0-9]", "");


            if (matricula.length() != 6) {
                return false;
            }


            if (matricula.matches("0{6}")) {
                return false;
            }


            if (!matricula.matches("[1-9]\\d{5}")) {
                return false;
            }

            return true;
        }
    }
}