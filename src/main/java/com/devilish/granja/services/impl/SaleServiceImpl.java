package com.devilish.granja.services.impl;

import com.devilish.granja.dto.request.SaleRequestDTO;
import com.devilish.granja.dto.response.SaleResponseDTO;
import com.devilish.granja.dto.response.SoldDuckResponseDTO;
import com.devilish.granja.entities.*;
import com.devilish.granja.repository.*;
import com.devilish.granja.services.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final SellerRepository sellerRepository;
    private final DuckRepository duckRepository;

    @Override
    public SaleResponseDTO save(SaleRequestDTO saleRequestDTO) {
        log.info("Iniciando método save para a venda");


        Client client = clientRepository.findById(saleRequestDTO.getClientId())
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado com o ID: {}", saleRequestDTO.getClientId());
                    return new RuntimeException("Cliente não encontrado: " + saleRequestDTO.getClientId());
                });


        Seller seller = sellerRepository.findById(saleRequestDTO.getSellerId())
                .orElseThrow(() -> {
                    log.error("Vendedor não encontrado com o ID: {}", saleRequestDTO.getSellerId());
                    return new RuntimeException("Vendedor não encontrado: " + saleRequestDTO.getSellerId());
                });


        List<Duck> ducks = duckRepository.findAllById(saleRequestDTO.getDuckIds());
        if (ducks.isEmpty()) {
            log.error("Nenhum pato encontrado com os IDs fornecidos: {}", saleRequestDTO.getDuckIds());
            throw new RuntimeException("Nenhum pato encontrado com os IDs fornecidos");
        }


        ducks.forEach(duck -> {
            if (duck.isSold()) {
                log.error("Pato já vendido: ID={}, Nome={}", duck.getId(), duck.getName());
                throw new RuntimeException("Pato já vendido: " + duck.getId());
            }
        });


        double totalValue = ducks.stream().mapToDouble(Duck::getPrice).sum();
        if (client.isDiscountEligible()) {
            totalValue *= 0.8;
            log.info("Desconto de 20% aplicado para o cliente: ID={}, Nome={}", client.getId(), client.getName());
        }


        Sale sale = Sale.builder()
                .dateSale(LocalDateTime.now())
                .client(client)
                .seller(seller)
                .ducks(ducks)
                .totalValue(totalValue)
                .build();


        ducks.forEach(duck -> duck.setSold(true));
        duckRepository.saveAll(ducks);


        Sale savedSale = saleRepository.save(sale);

        log.info("Venda salva com sucesso: ID={}, Data={}, Cliente ID={}, Vendedor ID={}, Valor Total={}",
                savedSale.getId(), savedSale.getDateSale(), savedSale.getClient().getId(), savedSale.getSeller().getId(), savedSale.getTotalValue());

        return convertToSaleResponseDTO(savedSale);
    }

    @Override
    public SaleResponseDTO findById(Long id) {
        log.info("Buscando venda com ID: {}", id);

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Venda não encontrada com o ID: {}", id);
                    return new RuntimeException("Venda não encontrada: " + id);
                });

        log.info("Venda encontrada: ID={}, Data={}, Cliente ID={}, Vendedor ID={}, Valor Total={}",
                sale.getId(), sale.getDateSale(), sale.getClient().getId(), sale.getSeller().getId(), sale.getTotalValue());

        return convertToSaleResponseDTO(sale);
    }

    @Override
    public List<SaleResponseDTO> findAll() {
        log.info("Listando todas as vendas");

        List<Sale> sales = saleRepository.findAll();

        log.info("Total de vendas encontradas: {}", sales.size());

        return sales.stream()
                .map(this::convertToSaleResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SaleResponseDTO update(Long id, SaleRequestDTO saleRequestDTO) {
        log.info("Atualizando venda com ID: {}", id);


        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Venda não encontrada com o ID: {}", id);
                    return new RuntimeException("Venda não encontrada: " + id);
                });

        log.info("Venda encontrada para atualização: ID={}, Data={}, Cliente ID={}, Vendedor ID={}, Valor Total={}",
                sale.getId(), sale.getDateSale(), sale.getClient().getId(), sale.getSeller().getId(), sale.getTotalValue());


        Client client = clientRepository.findById(saleRequestDTO.getClientId())
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado com o ID: {}", saleRequestDTO.getClientId());
                    return new RuntimeException("Cliente não encontrado: " + saleRequestDTO.getClientId());
                });


        Seller seller = sellerRepository.findById(saleRequestDTO.getSellerId())
                .orElseThrow(() -> {
                    log.error("Vendedor não encontrado com o ID: {}", saleRequestDTO.getSellerId());
                    return new RuntimeException("Vendedor não encontrado: " + saleRequestDTO.getSellerId());
                });


        List<Duck> ducks = duckRepository.findAllById(saleRequestDTO.getDuckIds());
        if (ducks.isEmpty()) {
            log.error("Nenhum pato encontrado com os IDs fornecidos: {}", saleRequestDTO.getDuckIds());
            throw new RuntimeException("Nenhum pato encontrado com os IDs fornecidos");
        }


        ducks.forEach(duck -> {
            if (duck.isSold() && !sale.getDucks().contains(duck)) {
                log.error("Pato já vendido em outra venda: ID={}, Nome={}", duck.getId(), duck.getName());
                throw new RuntimeException("Pato já vendido em outra venda: " + duck.getId());
            }
        });


        double totalValue = ducks.stream().mapToDouble(Duck::getPrice).sum();


        if (client.isDiscountEligible()) {
            totalValue *= 0.8;
            log.info("Desconto de 20% aplicado para o cliente: ID={}, Nome={}", client.getId(), client.getName());
        }


        sale.setDateSale(LocalDateTime.now());
        sale.setClient(client);
        sale.setSeller(seller);
        sale.setDucks(ducks);
        sale.setTotalValue(totalValue);


        ducks.forEach(duck -> duck.setSold(true));
        duckRepository.saveAll(ducks);


        Sale updatedSale = saleRepository.save(sale);

        log.info("Venda atualizada com sucesso: ID={}, Data={}, Cliente ID={}, Vendedor ID={}, Valor Total={}",
                updatedSale.getId(), updatedSale.getDateSale(), updatedSale.getClient().getId(), updatedSale.getSeller().getId(), updatedSale.getTotalValue());

        return convertToSaleResponseDTO(updatedSale);
    }

    @Override
    public void delete(Long id) {
        log.info("Excluindo venda com ID: {}", id);

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Venda não encontrada com o ID: {}", id);
                    return new RuntimeException("Venda não encontrada com o ID: " + id);
                });

        log.info("Venda encontrada para exclusão: ID={}, Data={}, Cliente ID={}, Vendedor ID={}, Valor Total={}",
                sale.getId(), sale.getDateSale(), sale.getClient().getId(), sale.getSeller().getId(), sale.getTotalValue());

        saleRepository.delete(sale);

        log.info("Venda excluída com sucesso: ID={}", id);
    }

    private SaleResponseDTO convertToSaleResponseDTO(Sale sale) {
        return SaleResponseDTO.builder()
                .id(sale.getId())
                .dateSale(sale.getDateSale())
                .clientId(sale.getClient().getId())
                .clientName(sale.getClient().getName())
                .sellerId(sale.getSeller().getId())
                .sellerName(sale.getSeller().getName())
                .duckIds(sale.getDucks().stream().map(Duck::getId).collect(Collectors.toList()))
                .duckNames(sale.getDucks().stream().map(Duck::getName).collect(Collectors.toList()))
                .totalValue(sale.getTotalValue())
                .build();
    }

    @Override
    public List<SoldDuckResponseDTO> findAllSoldDucks() {
        log.info("Listando todas as vendas de patos");


        List<Sale> sales = saleRepository.findAll();


        return sales.stream()
                .filter(sale -> sale.getDucks().stream().anyMatch(Duck::isSold))
                .map(sale -> {

                    List<SoldDuckResponseDTO.DuckInfoDTO> soldDucks = sale.getDucks().stream()
                            .filter(Duck::isSold)
                            .map(duck -> SoldDuckResponseDTO.DuckInfoDTO.builder()
                                    .name(duck.getName())
                                    .price(duck.getPrice())
                                    .build())
                            .collect(Collectors.toList());


                    return SoldDuckResponseDTO.builder()
                            .ducks(soldDucks)
                            .totalValue(sale.getTotalValue())
                            .client(SoldDuckResponseDTO.ClientInfoDTO.builder()
                                    .name(sale.getClient().getName())
                                    .build())
                            .build();
                })
                .collect(Collectors.toList());
    }

}