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

    @Override
    public SellerResponseDTO update(Long id, SellerRequestDTO sellerRequestDTO) {
        log.info("Atualizando vendedor com ID: {}", id);

        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Vendedor não encontrado com o ID: {}", id);
                    return new RuntimeException("Vendedor não encontrado: " + id);
                });

        log.info("Vendedor encontrado para atualização: ID={}, Nome={}, CPF={}, Matrícula={}",
                seller.getId(), seller.getName(), seller.getCpf(), seller.getRegistration());

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
    public void delete(Long id) {
        log.info("Excluindo vendedor com ID: {}", id);

        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Vendedor não encontrado com o ID: {}", id);
                    return new RuntimeException("Vendedor não encontrado com o ID: " + id);
                });

        log.info("Vendedor encontrado para exclusão: ID={}, Nome={}, CPF={}, Matrícula={}",
                seller.getId(), seller.getName(), seller.getCpf(), seller.getRegistration());

        sellerRepository.delete(seller);

        log.info("Vendedor excluído com sucesso: ID={}", id);
    }
}