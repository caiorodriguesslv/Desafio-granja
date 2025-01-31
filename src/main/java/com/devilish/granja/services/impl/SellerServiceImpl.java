package com.devilish.granja.services.impl;

import com.devilish.granja.dto.request.SellerRequestDTO;
import com.devilish.granja.dto.response.SellerResponseDTO;
import com.devilish.granja.entities.Seller;
import com.devilish.granja.repository.SellerRepository;
import com.devilish.granja.services.SellerService;
import com.devilish.granja.utils.Validator;
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


        if (!sellerRequestDTO.isValidCpfAndRegistration()) {
            log.error("Dados inválidos fornecidos para o vendedor: {}", sellerRequestDTO.getName());
            throw new RuntimeException("Dados inválidos. Verifique as informações fornecidas.");
        }


        sellerRepository.findByCpf(sellerRequestDTO.getCpf()).ifPresent(seller -> {
            log.error("Tentativa de cadastro com CPF já existente: {}", sellerRequestDTO.getCpf());
            throw new RuntimeException("Operação não permitida. Verifique os dados fornecidos.");
        });


        sellerRepository.findByRegistration(sellerRequestDTO.getRegistration()).ifPresent(seller -> {
            log.error("Tentativa de cadastro com matrícula já existente: {}", sellerRequestDTO.getRegistration());
            throw new RuntimeException("Operação não permitida. Verifique os dados fornecidos.");
        });


        Seller seller = Seller.builder()
                .name(sellerRequestDTO.getName())
                .cpf(sellerRequestDTO.getCpf())
                .registration(sellerRequestDTO.getRegistration())
                .build();


        Seller savedSeller = sellerRepository.save(seller);

        log.info("Vendedor salvo com sucesso: ID={}, Nome={}", savedSeller.getId(), savedSeller.getName());

        return SellerResponseDTO.builder()
                .id(savedSeller.getId())
                .name(savedSeller.getName())
                .cpf(savedSeller.getCpf())
                .registration(savedSeller.getRegistration())
                .build();
    }

    @Override
    public SellerResponseDTO update(Long id, SellerRequestDTO sellerRequestDTO) {
        log.info("Iniciando atualização do vendedor com ID: {}", id);


        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Vendedor não encontrado com o ID: {}", id);
                    return new RuntimeException("Operação não permitida. Verifique os dados fornecidos.");
                });

        log.info("Vendedor encontrado: ID={}, Nome={}", seller.getId(), seller.getName());


        if (!Validator.isValidCpf(sellerRequestDTO.getCpf())) {
            log.error("CPF inválido fornecido para atualização: {}", sellerRequestDTO.getCpf());
            throw new RuntimeException("Dados inválidos. Verifique as informações fornecidas.");
        }


        if (!Validator.isValidRegistration(sellerRequestDTO.getRegistration())) {
            log.error("Matrícula inválida fornecida para atualização: {}", sellerRequestDTO.getRegistration());
            throw new RuntimeException("Dados inválidos. Verifique as informações fornecidas.");
        }


        if (!seller.getCpf().equals(sellerRequestDTO.getCpf())) {
            sellerRepository.findByCpf(sellerRequestDTO.getCpf()).ifPresent(existingSeller -> {
                log.error("Tentativa de atualização com CPF já existente: {}", sellerRequestDTO.getCpf());
                throw new RuntimeException("Operação não permitida. Verifique os dados fornecidos.");
            });
        }


        if (!seller.getRegistration().equals(sellerRequestDTO.getRegistration())) {
            sellerRepository.findByRegistration(sellerRequestDTO.getRegistration()).ifPresent(existingSeller -> {
                log.error("Tentativa de atualização com matrícula já existente: {}", sellerRequestDTO.getRegistration());
                throw new RuntimeException("Operação não permitida. Verifique os dados fornecidos.");
            });
        }


        seller.setName(sellerRequestDTO.getName());
        seller.setCpf(sellerRequestDTO.getCpf());
        seller.setRegistration(sellerRequestDTO.getRegistration());


        Seller updatedSeller = sellerRepository.save(seller);

        log.info("Vendedor atualizado com sucesso: ID={}, Nome={}", updatedSeller.getId(), updatedSeller.getName());

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

        // Busca o vendedor
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Vendedor não encontrado com o ID: {}", id);
                    return new RuntimeException("Operação não permitida. Verifique os dados fornecidos.");
                });

        log.info("Vendedor encontrado: ID={}, Nome={}", seller.getId(), seller.getName());

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

        // Busca todos os vendedores
        List<Seller> sellers = sellerRepository.findAll();

        log.info("Total de vendedores encontrados: {}", sellers.size());

        return sellers.stream()
                .map(seller -> {
                    log.debug("Convertendo vendedor para DTO: ID={}, Nome={}", seller.getId(), seller.getName());
                    return SellerResponseDTO.builder()
                            .id(seller.getId())
                            .name(seller.getName())
                            .cpf(seller.getCpf())
                            .registration(seller.getRegistration())
                            .build();
                })
                .collect(Collectors.toList());
    }
}