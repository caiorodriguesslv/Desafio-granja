package com.devilish.granja.services.impl;

import com.devilish.granja.dto.request.ClientRequestDTO;
import com.devilish.granja.dto.response.ClientResponseDTO;
import com.devilish.granja.entities.Client;
import com.devilish.granja.repository.ClientRepository;
import com.devilish.granja.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {


    private final ClientRepository clientRepository;

    @Override
    public ClientResponseDTO save(ClientRequestDTO clientRequestDTO) {
        log.info("Iniciando método save para o cliente: {}", clientRequestDTO.getName());


        Client client = Client.builder()
                .name(clientRequestDTO.getName())
                .discountEligible(clientRequestDTO.isDiscountEligible())
                .build();


        Client savedClient = clientRepository.save(client);

        log.info("Cliente salvo com sucesso: ID={}, Nome={}, Elegível para Desconto={}",
                savedClient.getId(), savedClient.getName(), savedClient.isDiscountEligible());


        return ClientResponseDTO.builder()
                .id(savedClient.getId())
                .name(savedClient.getName())
                .discountEligible(savedClient.isDiscountEligible())
                .build();
    }

    @Override
    public ClientResponseDTO findById(Long id) {
        log.info("Buscando cliente com ID: {}", id);


        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado com o ID: {}", id);
                    return new RuntimeException("Cliente não encontrado: " + id);
                });

        log.info("Cliente encontrado: ID={}, Nome={}, Elegível para Desconto={}",
                client.getId(), client.getName(), client.isDiscountEligible());


        return ClientResponseDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .discountEligible(client.isDiscountEligible())
                .build();
    }

    @Override
    public List<ClientResponseDTO> findAll() {
        log.info("Listando todos os clientes");


        List<Client> clients = clientRepository.findAll();

        log.info("Total de clientes encontrados: {}", clients.size());


        return clients.stream()
                .map(client -> {
                    log.debug("Convertendo cliente para DTO: ID={}, Nome={}, Elegível para Desconto={}",
                            client.getId(), client.getName(), client.isDiscountEligible());
                    return ClientResponseDTO.builder()
                            .id(client.getId())
                            .name(client.getName())
                            .discountEligible(client.isDiscountEligible())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponseDTO update(Long id, ClientRequestDTO clientRequestDTO) {
        log.info("Atualizando cliente com ID: {}", id);


        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado com o ID: {}", id);
                    return new RuntimeException("Cliente não encontrado: " + id);
                });

        log.info("Cliente encontrado para atualização: ID={}, Nome={}, Elegível para Desconto={}",
                client.getId(), client.getName(), client.isDiscountEligible());


        client.setName(clientRequestDTO.getName());
        client.setDiscountEligible(clientRequestDTO.isDiscountEligible());


        Client updatedClient = clientRepository.save(client);

        log.info("Cliente atualizado com sucesso: ID={}, Nome={}, Elegível para Desconto={}",
                updatedClient.getId(), updatedClient.getName(), updatedClient.isDiscountEligible());


        return ClientResponseDTO.builder()
                .id(updatedClient.getId())
                .name(updatedClient.getName())
                .discountEligible(updatedClient.isDiscountEligible())
                .build();
    }

    @Override
    public void delete(Long id) {
        log.info("Excluindo cliente com ID: {}", id);


        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado com o ID: {}", id);
                    return new RuntimeException("Cliente não encontrado com o ID: " + id);
                });

        log.info("Cliente encontrado para exclusão: ID={}, Nome={}, Elegível para Desconto={}",
                client.getId(), client.getName(), client.isDiscountEligible());


        clientRepository.delete(client);

        log.info("Cliente excluído com sucesso: ID={}", id);
    }
}
